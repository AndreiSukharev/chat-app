package com.example.sbercloud.chat.service.chatbot;

import com.example.sbercloud.chat.model.Message;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
public interface ChatbotService {

    Message prepareResponse(Message requestMessage);
}
