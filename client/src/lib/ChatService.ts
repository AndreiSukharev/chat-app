import {
  ChatEvent,
  ChatEventHandler,
  ChatEventType,
  IChatService,
  IStorage,
  SendMessageServiceParams,
  SendTypingServiceParams,
  UpdateState,
  MessageEvent,
} from '@chatscope/use-chat';
import SockJS from 'sockjs-client';
import { SOCK_HOST } from '../env';
import { Stomp } from '@stomp/stompjs';

type EventHandlers = {
  onMessage: ChatEventHandler<ChatEventType.Message, ChatEvent<ChatEventType.Message>>;
  onConnectionStateChanged: ChatEventHandler<
    ChatEventType.ConnectionStateChanged,
    ChatEvent<ChatEventType.ConnectionStateChanged>
  >;
  onUserConnected: ChatEventHandler<ChatEventType.UserConnected, ChatEvent<ChatEventType.UserConnected>>;
  onUserDisconnected: ChatEventHandler<ChatEventType.UserDisconnected, ChatEvent<ChatEventType.UserDisconnected>>;
  onUserPresenceChanged: ChatEventHandler<
    ChatEventType.UserPresenceChanged,
    ChatEvent<ChatEventType.UserPresenceChanged>
  >;
  onUserTyping: ChatEventHandler<ChatEventType.UserTyping, ChatEvent<ChatEventType.UserTyping>>;
  [key: string]: any;
};

const subscriptions = new Map();
const topics = ['/conversations/messages'];
const client = Stomp.over(new SockJS(SOCK_HOST, null));

const processMessage = (msgBody: any) => {
  try {
    return JSON.parse(msgBody);
  } catch (e) {
    return msgBody;
  }
};

export class ChatService implements IChatService {
  storage?: IStorage;
  updateState: UpdateState;
  retryCount: number = 0;
  timeoutId?: ReturnType<typeof setTimeout>;

  eventHandlers: EventHandlers = {
    onMessage: () => {},
    onConnectionStateChanged: () => {},
    onUserConnected: () => {},
    onUserDisconnected: () => {},
    onUserPresenceChanged: () => {},
    onUserTyping: () => {},
  };

  constructor(storage: IStorage, update: UpdateState) {
    this.storage = storage;
    this.updateState = update;
    client.heartbeat.outgoing = 10000;
    client.heartbeat.incoming = 10000;

    client.connect(
      {},
      () => {
        topics.forEach((topic) => {
          this.subscribe(topic);
        });
      },
      (error: any) => {
        if (error) {
        }
      },
    );
  }

  setStorage(storage: IStorage) {
    this.storage = storage;
  }
  setUpdateState(update: UpdateState) {
    this.updateState = update;
  }

  sendMessage({ message, conversationId }: SendMessageServiceParams) {
    this.send('/app/conversations', JSON.stringify({ ...message, conversationId }));

    return message;
  }

  sendTyping({ isTyping, content, conversationId, userId }: SendTypingServiceParams) {
    const typingEvent = new CustomEvent('chat-protocol', {
      detail: {
        type: 'typing',
        isTyping,
        content,
        conversationId,
        userId,
        sender: this,
      },
    });

    window.dispatchEvent(typingEvent);
  }

  on<T extends ChatEventType, H extends ChatEvent<T>>(evtType: T, evtHandler: ChatEventHandler<T, H>) {
    const key = `on${evtType.charAt(0).toUpperCase()}${evtType.substring(1)}`;

    if (key in this.eventHandlers) {
      this.eventHandlers[key] = evtHandler;
    }
  }

  off<T extends ChatEventType, H extends ChatEvent<T>>(evtType: T, eventHandler: ChatEventHandler<T, H>) {
    const key = `on${evtType.charAt(0).toUpperCase()}${evtType.substring(1)}`;
    if (key in this.eventHandlers) {
      this.eventHandlers[key] = () => {};
    }
  }

  send(topic: string, msg: any, optHeaders?: any) {
    client.send(topic, optHeaders, msg);
  }

  subscribe = (topic: string) => {
    if (!subscriptions.has(topic)) {
      const self = this;
      let sub = client.subscribe(
        topic,
        (msg) => {
          const message = processMessage(msg.body);
          console.log(message);
          self.eventHandlers.onMessage(new MessageEvent({ message, conversationId: message.conversationId }));
        },
        {},
      );
      subscriptions.set(topic, sub);
    }
  };
}

let Service: ChatService;

export const serviceFactory = (storage: IStorage, updateState: UpdateState) => {
  if (!Service) {
    Service = new ChatService(storage, updateState);
    return Service;
  }
  Service.setStorage(storage);
  Service.setUpdateState(updateState);

  return Service;
};
