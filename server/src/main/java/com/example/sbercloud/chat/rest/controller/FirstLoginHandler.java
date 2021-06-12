package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class FirstLoginHandler implements UserPostSaveHandler {

    private final UserRepository userRepository;

    private final ConversationRepository conversationRepository;

    private final MessageRepository messageRepository;

    private UserEntity botUserEntity;

    private ConversationEntity commonConversationEntity;

    @PostConstruct
    private void init() {
        botUserEntity = userRepository.findById(-1L).orElseGet(this::createBotUser);
        commonConversationEntity = conversationRepository.findById(-1L).orElseGet(this::createCommonConversation);
    }

    private ConversationEntity createCommonConversation() {
        try {
            ConversationEntity conversationEntity = new ConversationEntity();
            conversationEntity.setId(-1L);
            conversationRepository.saveAndFlush(conversationEntity);
            return conversationEntity;
        } catch (Exception e) {
            return conversationRepository.findById(-1L).orElseThrow(
                    () -> new RuntimeException("Common conversation not found"));
        }
    }

    private UserEntity createBotUser() {
        try {
            UserEntity user = new UserEntity();
            user.setId(-1L);
            user.setUsername("chatBot");
            user.setFirstName("Чат");
            user.setLastName("Бот");
            user.setEmail("chatBot@gmail.com");
            userRepository.saveAndFlush(user);
            return user;
        } catch (Exception e) {
            return userRepository.findById(-1L).orElseThrow(
                    () -> new RuntimeException("Chat bot user not found"));
        }
    }

    public void handle(UserEntity newUser) {
//        final List<ConversationEntity> conversations = new ArrayList<>();
//        conversations.add(commonConversationEntity);
//        ConversationEntity chatBotConversationEntity = new ConversationEntity();
//        conversationRepository.saveAndFlush(chatBotConversationEntity);
//        conversations.add(chatBotConversationEntity);
//        newUser.setConversations(conversations);
//        userRepository.saveAndFlush(newUser);
//        chatBotConversationEntity.setParticipants(asList(botUserEntity, newUser));
//        conversationRepository.saveAndFlush(chatBotConversationEntity);
//        createMessage(chatBotConversationEntity, botUserEntity, "Приветствую вас в нашем чате!");
    }

    private void createMessage(ConversationEntity conversation, UserEntity sender, String content) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(content);
        messageEntity.setSender(sender);
        messageEntity.setConversation(conversation);
        messageRepository.save(messageEntity);
    }

}
