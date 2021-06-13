package com.example.sbercloud.chat.service.chatbot;

import com.example.sbercloud.chat.model.Message;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.sbercloud.chat.utility.Constant.CHATBOT_USER_ID;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
@RequiredArgsConstructor
@Service
public class SberChatbotService implements ChatbotService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;

    @Override
    public Message prepareResponse(Message requestMessage) {
        Message message = new Message();
        String responseContent = generateContent(requestMessage.getContent());
        message.setContent(responseContent);
        message.setConversationId(requestMessage.getConversationId());
        message.setSenderId(CHATBOT_USER_ID);
        return message;
    }

    private String generateContent(String content) {
        String generatedContent = null;
        if (isBlank(generatedContent) && content.contains("Помочь бизнесу")) {
            generatedContent = "Мы предлагаем сформировать СТП на твой сайт";
        }
        return "И тебе не хворать кожаный мешок";
    }
}
