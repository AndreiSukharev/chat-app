package com.example.sbercloud.chat.websocket.controller;

import com.example.sbercloud.chat.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@RequiredArgsConstructor
@Controller
public class ConversationWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/conversations")
    public void addMessageToConversation(@Payload Message message) {
        messagingTemplate.convertAndSend("/conversations/messages", message);
    }
}
