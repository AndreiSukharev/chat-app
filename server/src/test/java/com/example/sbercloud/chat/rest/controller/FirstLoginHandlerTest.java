package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.model.Conversation;
import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.Silent.class)
public class FirstLoginHandlerTest {

    @InjectMocks
    private FirstLoginHandler handler;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void testGenerateMockDataSavesToRepositories() {
        UserEntity chanBotUser = new UserEntity();
        Mockito.when(userRepository.findById(-1L)).thenReturn(Optional.of(chanBotUser));
        ConversationEntity commonConversation = new ConversationEntity();
        Mockito.when(conversationRepository.findById(-1L)).thenReturn(Optional.of(commonConversation));

        UserEntity newUser = new UserEntity();
        handler.handle(newUser);
        verify(userRepository, times(1)).save(newUser);
        verify(conversationRepository, times(1)).save(any());
        verify(messageRepository, times(1)).save(any());
    }
}