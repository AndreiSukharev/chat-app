import {
  ChatEvent,
  ChatEventHandler,
  ChatEventType,
  ChatMessage,
  IChatService,
  IStorage,
  MessageContentType,
  MessageDirection,
  MessageEvent,
  SendMessageServiceParams,
  SendTypingServiceParams,
  UpdateState,
  UserTypingEvent,
} from '@chatscope/use-chat';

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

export class ChatService implements IChatService {
  storage?: IStorage;
  updateState: UpdateState;

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

    // For communication we use CustomEvent dispatched to the window object.
    // It allows you to simulate sending and receiving data from the server.
    // In a real application, instead of adding a listener to the window,
    // you will implement here receiving data from your chat server.
    window.addEventListener('chat-protocol', (evt: Event) => {
      const event = evt as CustomEvent;

      const {
        detail: { type },
        detail,
      } = event;

      if (type === 'message') {
        const message = detail.message as ChatMessage<MessageContentType.TextHtml>;

        message.direction = MessageDirection.Incoming;
        const { conversationId } = detail;
        if (this.eventHandlers.onMessage && detail.sender !== this) {
          // Running the onMessage callback registered by ChatProvider will cause:
          // 1. Add a message to the conversation to which the message was sent
          // 2. If a conversation with the given id exists and is not active,
          //    its unreadCounter will be incremented
          // 3. Remove information about the sender who is writing from the conversation
          // 4. Re-render
          //
          // Note!
          // If a conversation with such id does not exist,
          // the message will be added, but the conversation object will not be created.
          // You have to take care of such a case yourself.
          // You can check here if there is already a conversation in storage.
          // If it is not there, you can create it before calling onMessage.
          // After adding a conversation to the list, you don't need to manually run updateState
          // because ChatProvider in onMessage will do it.
          this.eventHandlers.onMessage(new MessageEvent({ message, conversationId }));
        }
      } else if (type === 'typing') {
        const { userId, isTyping, conversationId, content, sender } = detail;

        if (this.eventHandlers.onUserTyping && sender !== this) {
          // TODO: Przetłumaczyć
          // Uruchomienie callbacka onUserTyping zarejestrowanego przez ChatProvider spowoduje
          // 1. Dodanie użytkownika do listy piszących w danej konwersacji
          // 2. Debounce
          // 3. Przerenderowanie
          this.eventHandlers.onUserTyping(
            new UserTypingEvent({
              userId,
              isTyping,
              conversationId,
              content,
            }),
          );
        }
      }
    });
  }

  sendMessage({ message, conversationId }: SendMessageServiceParams) {
    const messageEvent = new CustomEvent('chat-protocol', {
      detail: {
        type: 'message',
        message,
        conversationId,
        sender: this,
      },
    });

    window.dispatchEvent(messageEvent);

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
}
