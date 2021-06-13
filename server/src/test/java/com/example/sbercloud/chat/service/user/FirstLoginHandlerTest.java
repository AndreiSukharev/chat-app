package com.example.sbercloud.chat.service.user;

import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.*;
import com.example.sbercloud.chat.service.user.FirstLoginHandler;
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
    private SimpleParticipantRepository participantRepository;

    @Mock
    private SimpleMessageRepository messageRepository;

    @Test
    public void testGenerateMockDataSavesToRepositories() {
        UserEntity chatBotUser = new UserEntity();
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(chatBotUser));
        ConversationEntity commonConversation = new ConversationEntity();
        Mockito.when(conversationRepository.findById(1L)).thenReturn(Optional.of(commonConversation));

        handler.init();
        UserEntity newUser = new UserEntity();
        handler.handle(newUser);
        verify(conversationRepository, times(1)).save(any());
        verify(messageRepository, times(1)).save(any());
        verify(participantRepository, times(1)).saveAll(any());
    }
}