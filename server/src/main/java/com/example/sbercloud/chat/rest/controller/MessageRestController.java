package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import com.example.sbercloud.chat.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/messages")
public class MessageRestController {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    @GetMapping("/{messageId}")
    public Message findMessage(@PathVariable long messageId){
        Optional<MessageEntity> messageEntity = messageRepository.findById(messageId);
        Message message = mapMessageEntityToMessage(messageEntity.get());
        return message;
    }

    @PutMapping
    public void addMessage(@RequestBody Message message) {
        MessageEntity messageEntity = mapMessageToMessageEntity(message);
        MessageEntity save = messageRepository.save(messageEntity);
    }

    private MessageEntity mapMessageToMessageEntity(Message message) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setContent(message.getContent());
        long senderId = Long.parseLong(message.getSenderId());
        Optional<UserEntity> byId = userRepository.findById(senderId);
        // TODO: 12/06/2021 тут упадём если пользователя нет
        messageEntity.setSender(byId.get());
        return messageEntity;
    }

    private Message mapMessageEntityToMessage(MessageEntity messageEntity) {
        Message message = new Message();
        message.setContent(messageEntity.getContent());
        message.setSenderId(String.valueOf(messageEntity.getSender().getId()));
        return message;
    }
}
