package com.example.sbercloud.chat.websocket.controller;

import com.example.sbercloud.chat.model.Message;
import com.example.sbercloud.chat.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@RequiredArgsConstructor
@Controller
public class ConversationWebSocketController {

    private final MessageService messageService;

    @MessageMapping("/conversations")
    public void addMessageToConversation(@Payload Message message) {
        messageService.processMessage(message);
    }
}
