package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.model.Message;
import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.MessageRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @GetMapping("conversation/{conversationId}")
    public List<Message> getMessagesByConversationId(@PathVariable long conversationId,
                                                          @RequestParam(value = "page", defaultValue = "0", required = false) int page,
                                                          @RequestParam(value = "pageSize", defaultValue = "20", required = false) int pageSize) {
        PageRequest pageRequest = PageRequest.of(page, pageSize);
        Page<MessageEntity> searchMessageEntityResult = messageRepository.findAllByConversation_Id(pageRequest, conversationId);
        List<Message> foundMessages = searchMessageEntityResult.getContent().stream().map(this::mapMessageEntityToMessage).collect(Collectors.toList());
        return foundMessages;
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
