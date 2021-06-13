import React, { useEffect, useState } from 'react';
import './themes/default/main.scss';
import './App.css';
import './css/MessageStyle.css';
import { Box, createMuiTheme, MuiThemeProvider } from '@material-ui/core';
import { nanoid } from 'nanoid';
import {
  AutoDraft,
  ChatMessage,
  Conversation,
  ConversationId,
  ConversationRole,
  MessageContentType,
  Participant,
  Presence,
  ChatProvider,
  TypingUsersList,
  User,
  UserStatus,
  BasicStorage,
} from '@chatscope/use-chat';
import { serviceFactory } from './lib/ChatService';
import { Chat } from './components/Chat';
import Login from './components/login/Login';
import httpClient from './services/httpClient';
import userAvatar from './assets/avatar.svg';

const messageIdGenerator = (message: ChatMessage<MessageContentType>) => nanoid();
const groupIdGenerator = () => nanoid();
const userStorage = new BasicStorage({ groupIdGenerator, messageIdGenerator });

// Create serviceFactory

const user = new User({
  id: '2',
  presence: new Presence({ status: UserStatus.Available, description: '' }),
  firstName: '',
  lastName: '',
  username: '',
  email: '',
  avatar: userAvatar,
  bio: 'Очень интересно',
});

const chat = { name: 'User', storage: userStorage, id: '2' };

function createConversation(id: ConversationId, userId: string): Conversation {
  return new Conversation({
    id,
    participants: [
      new Participant({
        id: userId,
        role: new ConversationRole([]),
      }),
    ],
    unreadCounter: 0,
    typingUsers: new TypingUsersList({ items: [] }),
    draft: '',
  });
}

const init = (users: User[]) => {
  // Add users and conversations to the states
  users.forEach((u) => {
    if (u.id !== chat.id) {
      chat.storage.addUser(
        new User({
          id: u.id,
          presence: new Presence({ status: UserStatus.Available, description: '' }),
          firstName: '',
          lastName: '',
          username: u.username,
          email: '',
          avatar: u.avatar || userAvatar,
          bio: 'Ух ты ' + u.id,
        }),
      );

      const conversationId = u.id;

      const myConversation = chat.storage.getState().conversations.find((cv) => {
        return typeof cv.participants.find((p) => p.id === u.id) !== 'undefined';
      });
      if (!myConversation) {
        chat.storage.addConversation(createConversation(conversationId, u.id));

        if (chat) {
          const hisConversation = chat.storage
            .getState()
            .conversations.find((cv) => typeof cv.participants.find((p) => p.id === chat.id) !== 'undefined');
          if (!hisConversation) {
            chat.storage.addConversation(createConversation(conversationId, chat.id));
          }
        }
      }
    }
  });
};

const theme = createMuiTheme({
  palette: {
    primary: {
      main: '#00EA95',
    },
    secondary: {
      main: '#D2D2D2',
    },
  },
});

const App = () => {
  const [author, setAuthor] = useState('');
  const [users, setUsers] = useState<User[]>([]);
  const [conversations, setConversations] = useState<Conversation[]>([]);

  useEffect(() => {
    user.username = author;
  }, [author]);

  useEffect(() => {
    httpClient.get('/users').then((result) => setUsers(result.data));
  }, []);

  useEffect(() => {
    if (users.length !== 0) {
      init(users);
      setAuthor('vlad');
    }
  }, [users]);

  useEffect(() => {
    httpClient.get('/conversations').then((result) => setConversations(result.data));
  }, []);

  return (
    <MuiThemeProvider theme={theme}>
      <>
        <div>
          {/*<Login/>*/}
          {/*<NameComponent setName={setAuthor} />*/}
        </div>
        {author && (
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
            <Box height="100vh" overflow="hidden">
              <Chat user={user} />
            </Box>
          </ChatProvider>
        )}
      </>
    </MuiThemeProvider>
  );
};

export default App;
