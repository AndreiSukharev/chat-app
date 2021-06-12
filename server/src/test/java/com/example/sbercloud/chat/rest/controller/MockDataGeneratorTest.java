package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.dal.repository.ConversationRepository;
import com.example.sbercloud.chat.dal.repository.MessageRepository;
import com.example.sbercloud.chat.dal.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class MockDataGeneratorTest {

    @InjectMocks
    private MockDataGenerator mockDataGenerator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConversationRepository conversationRepository;

    @Mock
    private MessageRepository messageRepository;

    @Test
    public void testGenerateMockDataSavesToRepositories() {
        mockDataGenerator.generateMockData("John");
        verify(userRepository, times(1)).saveAll(any());
        verify(conversationRepository, times(1)).saveAll(any());
        verify(messageRepository, times(1)).saveAll(any());
    }
}