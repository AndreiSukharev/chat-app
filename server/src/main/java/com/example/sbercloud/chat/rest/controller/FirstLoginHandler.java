package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import com.example.sbercloud.chat.persistence.entity.SimpleMessageEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.SimpleMessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class FirstLoginHandler implements UserPostSaveHandler {

    public static final long FIRST_ID = 1L;
    private final UserRepository userRepository;

    private final ConversationRepository conversationRepository;

    private final SimpleMessageRepository messageRepository;

    private UserEntity botUserEntity;

    private ConversationEntity commonConversationEntity;

    @PostConstruct
    private void init() {
        botUserEntity = userRepository.findById(FIRST_ID).orElseGet(this::createBotUser);
        commonConversationEntity = conversationRepository.findById(FIRST_ID).orElseGet(this::createCommonConversation);
    }

    private ConversationEntity createCommonConversation() {
        try {
            ConversationEntity conversationEntity = new ConversationEntity();
            conversationEntity.setId(FIRST_ID);
            conversationRepository.saveAndFlush(conversationEntity);
            return conversationEntity;
        } catch (Exception e) {
            return conversationRepository.findById(FIRST_ID).orElseThrow(
                    () -> new RuntimeException("Common conversation not found"));
        }
    }

    private UserEntity createBotUser() {
        try {
            UserEntity user = new UserEntity();
            user.setId(FIRST_ID);
            user.setUsername("chatBot");
            user.setFirstName("Чат");
            user.setLastName("Бот");
            user.setEmail("chatBot@gmail.com");
            userRepository.saveAndFlush(user);
            return user;
        } catch (Exception e) {
            return userRepository.findById(FIRST_ID).orElseThrow(
                    () -> new RuntimeException("Chat bot user not found"));
        }
    }

    public void handle(UserEntity newUser) {
        final List<ConversationEntity> conversations = new ArrayList<>();
        conversations.add(commonConversationEntity);
        ConversationEntity chatBotConversationEntity = new ConversationEntity();
        conversationRepository.saveAndFlush(chatBotConversationEntity);
        conversations.add(chatBotConversationEntity);
        newUser.setConversations(conversations);
        userRepository.saveAndFlush(newUser);
        chatBotConversationEntity.setParticipants(asList(botUserEntity, newUser));
        conversationRepository.saveAndFlush(chatBotConversationEntity);
        createMessage(chatBotConversationEntity, botUserEntity, "Приветствую вас в нашем чате!");
    }

    private void createMessage(ConversationEntity conversation, UserEntity sender, String content) {
        SimpleMessageEntity messageEntity = new SimpleMessageEntity();
        messageEntity.setContent(content);
        messageEntity.setUserId(1L);
        messageEntity.setConversationId(conversation.getId());
        messageRepository.save(messageEntity);
    }

}
