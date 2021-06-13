package com.example.sbercloud.chat.service;

import com.example.sbercloud.chat.model.Message;
import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import com.example.sbercloud.chat.service.chatbot.ChatbotService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import static com.example.sbercloud.chat.model.ConversationType.BOT;
import static java.text.MessageFormat.format;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class ConversationMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ChatbotService chatbotService;
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void processMessage(Message message) {
//        MessageEntity messageEntity = mapMessageToMessageEntity(message);
//        messageRepository.save(messageEntity);
        if (false) {
            log.debug("This is a conversation with a chat bot. Generating a response...");
            Message chatbotResponse = chatbotService.prepareResponse(message);
//            MessageEntity responseEntity = mapMessageToMessageEntity(message);
//            messageRepository.save(responseEntity);
            sendMessage(chatbotResponse);
        } else {
            sendMessage(message);
        }
    }

    private MessageEntity mapMessageToMessageEntity(Message message) {
        MessageEntity messageEntity = new MessageEntity();
        UserEntity userEntity = userRepository.findById(Long.parseLong(message.getSenderId())).get();
        messageEntity.setSender(userEntity);
        ConversationEntity conversationEntity = conversationRepository.findById(Long.parseLong(message.getConversationId())).get();
        messageEntity.setConversation(conversationEntity);
        messageEntity.setContent(message.getContent());
        return messageEntity;
    }

    private boolean isConversationWithChatbot(ConversationEntity conversationEntity) {
        return conversationEntity.getType().equals(BOT);
    }

    private void sendMessage(Message message) {
        String conversationMessagesChannel = "/conversations/messages";
        log.debug(format("Sending a message to the channel. Channel: [{0}], message: [{1}]", conversationMessagesChannel, message));
        messagingTemplate.convertAndSend(conversationMessagesChannel, message);
    }
}
