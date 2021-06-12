package com.example.sbercloud.chat.websocket.controller;

import com.example.sbercloud.chat.dal.entity.ConversationEntity;
import com.example.sbercloud.chat.model.Conversation;
import com.example.sbercloud.chat.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
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

    @MessageMapping("/conversations/{conversationId}")
    public void addMessageToConversation(@Payload Message message, @DestinationVariable("conversationId") String conversationId) {
        // path example: /conversations/1/messages
        messagingTemplate.convertAndSend("/conversations/" + conversationId + "/messages", message);
    }

    private Conversation mapConversationEntityToConversation(ConversationEntity conversationEntity) {
        Conversation conversation = new Conversation();
        conversation.setId(conversation.getId());
        conversation.setParticipants(conversation.getParticipants());
        return conversation;
    }
}
