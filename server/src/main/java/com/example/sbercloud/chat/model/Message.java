package com.example.sbercloud.chat.model;

import lombok.Data;

@Data
public class Message {

    /**
     * Идентификатор беседы
     */
    private long conversationId;

    private long senderId;

    private String content;

}
