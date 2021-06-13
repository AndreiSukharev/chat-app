# API
## WebSocket
Путь для подключения к сокету - ***/socket***  
Путь для отправки сообщения - ***/app/conversations***  
Топик для подписки и получения сообщений - ***/conversations/messages***  
Структура сообщения:
```json
{
  "conversationId": 1,
  "senderId": 1,
  "content": "Я контент"
}
```

## Endpoints


 ### Sign In
```
POST     /api/signin
body: {
 "login": "admin",
 "password": "password"
}
```

 ### Log out
```
POST     /api/logout
```

### Users

```
GET     /api/users
GET     /api/users/<id>
```

Example response:
```
{
    "id": 4,
    "login": "test",
    "name": "asdasd",
    "email": "test@gmail.com",
   
}
```

### Chats

```
GET     /api/chats
GET    /api/chats/<id>
```
Example GET all chats response
```
[
    {
       "id": 3
        "chatName": "kek",
    },
]
```

Example GET by id response
```
{
    "messages": [
        {
            "createdAt": "2019-08-07 15:34:07",
            "text": "asdad",
            "author": "test1"
        },
        {
            "createdAt": "2019-08-07 15:34:11",
            "text": "asdd",
            "author": "test2"
        }
    ],
}
```

### Socket

```
/socket -> connect to socket
    
```
Events

on:

```
'getMessage(message)' -> get message from server to chat
```

emit:

```
'join(id)' -> send chat id to join the chat
```

```
'sendMessage(message)' -> send message 
{
    "text": "Hello",
    "chatId": "1",
    "author": "admin",
    "createdAt": "2019-09-02T09:25:07.561Z"
}
```


