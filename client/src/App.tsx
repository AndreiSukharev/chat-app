import React, { useEffect, useState } from 'react';
import './themes/default/main.scss';
import './App.css';
import './css/MessageStyle.css';
import NameComponent from './components/NameComponent';
import { Box, createMuiTheme, MuiThemeProvider } from '@material-ui/core';
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
import { Chat } from './components/Chat';
import Login from "./components/login/Login";

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

  useEffect(() => {
    user.username = author;
  }, [author]);

  return (
    <MuiThemeProvider theme={theme}>
      <div>
        <Login/>
        {/*<NameComponent setName={setAuthor} />*/}
      </div>
      {author && (
        <Box height="100vh" overflow="hidden">
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
        </Box>
      )}
    </MuiThemeProvider>
  );
};

export default App;
