import React, { useEffect, useRef, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import './themes/default/main.scss';

// @ts-ignore
import SockJsClient from 'react-stomp';
import './App.css';
import './css/MessageStyle.css';
import NameComponent from './components/NameComponent';
import { nanoid } from 'nanoid';
import {
  AutoDraft,
  ChatMessage,
  Conversation,
  ConversationId,
  ConversationRole,
  IStorage,
  MessageContentType,
  Participant,
  Presence,
  ChatProvider,
  TypingUsersList,
  UpdateState,
  User,
  UserStatus,
} from '@chatscope/use-chat';
import { ChatService } from './lib/ChatService';
import { Storage } from './lib/Storage';
import { userModel, users } from './data/data';
import { Container, Row, Col } from 'react-bootstrap';
import { Chat } from './components/Chat';

// sendMessage and addMessage methods can automagically generate id for messages and groups
// This allows you to omit doing this manually, but you need to provide a message generator
// The message id generator is a function that receives message and returns id for this message
// The group id generator is a function that returns string
const messageIdGenerator = (message: ChatMessage<MessageContentType>) => nanoid();
const groupIdGenerator = () => nanoid();
const userStorage = new Storage({ groupIdGenerator, messageIdGenerator });

// Create serviceFactory
const serviceFactory = (storage: IStorage, updateState: UpdateState) => {
  return new ChatService(storage, updateState);
};

const user = new User({
  id: userModel.name,
  presence: new Presence({ status: UserStatus.Available, description: '' }),
  firstName: '',
  lastName: '',
  username: userModel.name,
  email: '',
  avatar: userModel.avatar,
  bio: 'Очень интересно',
});

const chat = { name: 'User', storage: userStorage };

function createConversation(id: ConversationId, name: string): Conversation {
  return new Conversation({
    id,
    participants: [
      new Participant({
        id: name,
        role: new ConversationRole([]),
      }),
    ],
    unreadCounter: 0,
    typingUsers: new TypingUsersList({ items: [] }),
    draft: '',
  });
}

// Add users and conversations to the states
users.forEach((u) => {
  if (u.name !== chat.name) {
    chat.storage.addUser(
      new User({
        id: u.name,
        presence: new Presence({ status: UserStatus.Available, description: '' }),
        firstName: '',
        lastName: '',
        username: u.name,
        email: '',
        avatar: u.avatar,
        bio: 'Ух ты',
      }),
    );

    const conversationId = nanoid();

    const myConversation = chat.storage
      .getState()
      .conversations.find((cv) => typeof cv.participants.find((p) => p.id === u.name) !== 'undefined');
    if (!myConversation) {
      chat.storage.addConversation(createConversation(conversationId, u.name));

      if (chat) {
        const hisConversation = chat.storage
          .getState()
          .conversations.find((cv) => typeof cv.participants.find((p) => p.id === chat.name) !== 'undefined');
        if (!hisConversation) {
          chat.storage.addConversation(createConversation(conversationId, chat.name));
        }
      }
    }
  }
});

const App = () => {
  const [messages, setMessages] = useState<any[]>([]);
  const [author, setAuthor] = useState('');
  const clientRef = useRef<any>(null);

  useEffect(() => {
    user.username = author;
  }, [author]);

  const sendMessage = ({ author, text }: { author: string; text: string }) => {
    clientRef.current.sendMessage(
      '/app/sendMessage',
      JSON.stringify({
        author: author,
        text: text,
        createdAt: new Date(),
      }),
    );
  };

  const displayMessages = () => {
    return (
      <div>
        {messages.map((msg) => {
          return (
            <div key={msg.createdAt}>
              {author === msg.author ? (
                <div>
                  <p className="title1">{msg.author} : </p>
                  <br />
                  <p>{msg.text}</p>
                </div>
              ) : (
                <div>
                  <p className="title2">{msg.author} : </p>
                  <br />
                  <p>{msg.text}</p>
                </div>
              )}
            </div>
          );
        })}
      </div>
    );
  };

  return (
    <>
      <div>
        <NameComponent setName={setAuthor} />
        <div className="align-center">
          <h1>Sber chat</h1>
          <br />
          <br />
        </div>
        <SockJsClient
          url="http://45.9.27.185:31765/socket/"
          topics={['/topic/getMessage']}
          onConnect={() => {
            console.log('connected');
          }}
          onDisconnect={() => {
            console.log('Disconnected');
          }}
          onMessage={(msg: any) => {
            setMessages([...messages, msg]);
          }}
          ref={(client: any) => {
            clientRef.current = client;
          }}
        />
      </div>
      {author && (
        <Container fluid className="h-100 p-4">
          <Row className="h-50 pb-2 flex-nowrap">
            <Col>
              <ChatProvider
                serviceFactory={serviceFactory}
                storage={userStorage}
                config={{
                  typingThrottleTime: 250,
                  typingDebounceTime: 900,
                  debounceTyping: true,
                  autoDraft: AutoDraft.Save | AutoDraft.Restore,
                }}
              >
                <Chat user={user} />
              </ChatProvider>
            </Col>
          </Row>
        </Container>
      )}
      <div className="align-center">
        User: <p className="title1">{author}</p>
      </div>
      <div className="align-center">{displayMessages()}</div>
    </>
  );
};

export default App;
