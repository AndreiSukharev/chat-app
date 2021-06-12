package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.dal.entity.ConversationEntity;
import com.example.sbercloud.chat.dal.entity.MessageEntity;
import com.example.sbercloud.chat.dal.entity.UserEntity;
import com.example.sbercloud.chat.dal.repository.ConversationRepository;
import com.example.sbercloud.chat.dal.repository.MessageRepository;
import com.example.sbercloud.chat.dal.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static java.util.Arrays.asList;

@RequiredArgsConstructor
@RestController
@RequestMapping("/generateMockData")
public class MockDataGenerator {

    private UserRepository userRepository;

    private ConversationRepository conversationRepository;

    private MessageRepository messageRepository;

    public void generateMockData(String login) {
        UserEntity mainUser = getUser(login);
        UserEntity privateConversationUser = getUser(login + "OneToOne");
        UserEntity firstGroupConversationUser = getUser(login + "GroupFirst");
        UserEntity secondGroupConversationUser = getUser(login + "GroupSecond");

        final List<UserEntity> users = asList(
                mainUser, privateConversationUser, firstGroupConversationUser, secondGroupConversationUser);
        userRepository.saveAll(users);
        ConversationEntity privateConversation = new ConversationEntity();
        privateConversation.setParticipants(asList(mainUser, privateConversationUser));
        ConversationEntity groupConversation = new ConversationEntity();
        groupConversation.setParticipants(asList(mainUser, firstGroupConversationUser, secondGroupConversationUser));
        conversationRepository.saveAll(asList(privateConversation, groupConversation));
        MessageEntity pm1 = createMessage(privateConversation, mainUser, "Привет");
        MessageEntity pm2 = createMessage(privateConversation, privateConversationUser, "Салют");
        MessageEntity pm3 = createMessage(privateConversation, mainUser,
                "Что думаешь о sbercloud?");
        MessageEntity pm4 = createMessage(privateConversation, privateConversationUser,
                "У них все получится!");
        MessageEntity gm1 = createMessage(privateConversation, mainUser, "Привет");
        MessageEntity gm2 = createMessage(privateConversation, firstGroupConversationUser, "Салют");
        MessageEntity gm3 = createMessage(privateConversation, secondGroupConversationUser,
                "И снова зравствуйте");
        MessageEntity gm4 = createMessage(privateConversation, mainUser,
                "Как вам новый чат?");
        MessageEntity gm5 = createMessage(privateConversation, firstGroupConversationUser,
                "он прекрасен");
        MessageEntity gm6 = createMessage(privateConversation, firstGroupConversationUser,
                "убийца телеграма");
        messageRepository.saveAll(asList(pm1, pm2, pm3, pm4, gm1, gm2, gm3, gm4, gm5, gm6));

    }

    private MessageEntity createMessage(ConversationEntity conversation, UserEntity sender, String content) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(content);
        messageEntity.setSender(sender);
        messageEntity.setConversation(conversation);
        return messageEntity;
    }

    private UserEntity getUser(String login) {
        UserEntity user = new UserEntity();
        user.setUsername(login);
        user.setFirstName(login + " FirstName");
        user.setLastName(login + " LastName");
        user.setEmail(login + "@gmail.com");
        return user;
    }

}
