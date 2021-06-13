package com.example.sbercloud.chat.model;

import lombok.Data;

@Data
public class Message {

    /**
     * Идентификатор беседы
     */
    private String conversationId;

    /**
     * Идентификатор пользователя, который отправил сообщение
     */
    private String senderId;

    /**
     * Контент сообщения
     */
    private String content;

}
