package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.dal.entity.ConversationEntity;
import com.example.sbercloud.chat.dal.entity.UserEntity;
import com.example.sbercloud.chat.dal.repository.ConversationRepository;
import com.example.sbercloud.chat.dal.repository.UserRepository;
import com.example.sbercloud.chat.model.Conversation;
import com.example.sbercloud.chat.model.ConversationSpec;
import com.example.sbercloud.chat.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/conversation")
public class ConversationRestController {

    private final ConversationRepository conversationRepository;

    private final UserRepository userRepository;

    @GetMapping("/{conversationId}")
    public Conversation findConversation(@PathVariable long conversationId) {
        Conversation conversation = mapConversationEntityToConversation(conversationRepository.findById(conversationId).get());
        return conversation;

    }

    @PutMapping
    public void createConversation(@RequestBody ConversationSpec conversationSpec) {
        ConversationEntity conversationEntity = mapConversationSpecToConversationEntity(conversationSpec);
        conversationRepository.save(conversationEntity);
    }

    private ConversationEntity mapConversationSpecToConversationEntity(ConversationSpec conversationSpec) {
        ConversationEntity conversationEntity = new ConversationEntity();
        List<UserEntity> participants = conversationSpec.getParticipantIds().stream()
                .map(this::mapParticipantIdToUserEntity)
                .collect(Collectors.toList());
        conversationEntity.setParticipants(participants);
        return conversationEntity;
    }

    private UserEntity mapParticipantIdToUserEntity(Long participantId) {
        // TODO: 12/06/2021 тут упадём если пользователя нет
        return userRepository.findById(participantId).get();
    }

    private Conversation mapConversationEntityToConversation(ConversationEntity conversationEntity) {
        Conversation conversation = new Conversation();
        List<User> participants = conversationEntity.getParticipants().stream()
                .map(this::mapUserEntityToUser)
                .collect(Collectors.toList());
        conversation.setId(conversationEntity.getId());
        conversation.setParticipants(participants);
        return conversation;
    }

    private User mapUserEntityToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        return user;
    }

}
