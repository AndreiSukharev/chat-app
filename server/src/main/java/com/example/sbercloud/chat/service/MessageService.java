package com.example.sbercloud.chat.service;

import com.example.sbercloud.chat.model.Message;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
public interface MessageService {

    void processMessage(Message message);
}
