package com.example.sbercloud.chat.service.user;

import com.example.sbercloud.chat.persistence.entity.*;
import com.example.sbercloud.chat.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

import static java.util.Arrays.asList;

@Service
@RequiredArgsConstructor
public class FirstLoginHandler implements UserPostSaveHandler {

    public static final long FIRST_ID = 1L;

    private final UserRepository userRepository;

    private final SimpleParticipantRepository simpleParticipantRepository;

    private final ConversationRepository conversationRepository;

    private final SimpleMessageRepository messageRepository;

    private UserEntity botUserEntity;

    private ConversationEntity commonConversationEntity;

    @PostConstruct
    public void init() {
        commonConversationEntity = conversationRepository.findById(FIRST_ID).orElseGet(this::createCommonConversation);
        botUserEntity = userRepository.findById(FIRST_ID).orElseGet(this::createBotUser);
    }

    private ConversationEntity createCommonConversation() {
        try {
            ConversationEntity conversationEntity = new ConversationEntity();
            conversationEntity.setId(FIRST_ID);
            conversationRepository.save(conversationEntity);
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
            userRepository.save(user);
            SimpleParticipantEntity botInCommonChat = new SimpleParticipantEntity();
            botInCommonChat.setId(botUserEntity.getId());
            botInCommonChat.setConversationId(commonConversationEntity.getId());
            simpleParticipantRepository.save(botInCommonChat);
            return user;
        } catch (Exception e) {
            return userRepository.findById(FIRST_ID).orElseThrow(
                    () -> new RuntimeException("Chat bot user not found"));
        }
    }

    public void handle(UserEntity newUser) {
        ConversationEntity chatBotConversationEntity = new ConversationEntity();
        conversationRepository.save(chatBotConversationEntity);
        createParticipants(chatBotConversationEntity, newUser);
        createMessage(chatBotConversationEntity, botUserEntity, "Приветствую вас в нашем чате!");
    }

    private void createParticipants(ConversationEntity conversationWithBot, UserEntity newUser) {
        SimpleParticipantEntity first = new SimpleParticipantEntity();
        first.setUserId(botUserEntity.getId());
        first.setConversationId(conversationWithBot.getId());
        SimpleParticipantEntity second = new SimpleParticipantEntity();
        second.setUserId(newUser.getId());
        second.setConversationId(conversationWithBot.getId());
        SimpleParticipantEntity third = new SimpleParticipantEntity();
        third.setUserId(newUser.getId());
        third.setConversationId(commonConversationEntity.getId());
        simpleParticipantRepository.saveAll(asList(first, second, third));
    }

    private void createMessage(ConversationEntity conversation, UserEntity sender, String content) {
        SimpleMessageEntity messageEntity = new SimpleMessageEntity();
        messageEntity.setContent(content);
        messageEntity.setUserId(1L);
        messageEntity.setConversationId(conversation.getId());
        messageRepository.save(messageEntity);
    }

}
