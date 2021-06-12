package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import com.example.sbercloud.chat.model.Conversation;
import com.example.sbercloud.chat.model.ConversationSpec;
import com.example.sbercloud.chat.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/conversations")
public class ConversationRestController {

    private final ConversationRepository conversationRepository;

    private final UserRepository userRepository;

    @GetMapping("/{conversationId}")
    public Conversation findConversation(@PathVariable long conversationId) {
        Optional<ConversationEntity> conversationSearchResult = conversationRepository.findById(conversationId);
        Conversation foundConversation = conversationSearchResult
                .map(this::mapConversationEntityToConversation).orElseThrow(() -> new RuntimeException(format("Conversation is not found. Conversation id: [{0}]", conversationId)));
        log.debug(format("Search result for conversations by id[{0}]. Found conversations: [{1}]", conversationId, foundConversation));
        return foundConversation;
    }

    @GetMapping("user/{userId}")
    public List<Conversation> findConversationByUserId(@PathVariable long userId) {
        List<Conversation> foundConversations = StreamSupport.stream(conversationRepository.findAll().spliterator(), false)
                .filter(conversationEntity -> isContainsUser(conversationEntity.getParticipants(), userId))
                .map(this::mapConversationEntityToConversation)
                .collect(toList());
        log.debug(format("Search result for conversations by user id[{0}]. Found conversations: [{1}]", userId, foundConversations));
        return foundConversations;
    }

    @GetMapping
    public List<Conversation> getConversations() {
        List<Conversation> foundConversations = StreamSupport.stream(conversationRepository.findAll().spliterator(), false)
                .map(this::mapConversationEntityToConversation)
                .collect(toList());
        log.debug(format("Found conversations: [{0}]", foundConversations));
        return foundConversations;

    }

    @PutMapping
    public void createConversation(@RequestBody ConversationSpec conversationSpec) {
        ConversationEntity conversationEntity = mapConversationSpecToConversationEntity(conversationSpec);
        conversationRepository.save(conversationEntity);
    }

    private boolean isContainsUser(List<UserEntity> userEntities, long userId) {
        return userEntities.stream().anyMatch(userEntity -> userEntity.getId() == userId);
    }

    private ConversationEntity mapConversationSpecToConversationEntity(ConversationSpec conversationSpec) {
        ConversationEntity conversationEntity = new ConversationEntity();
        List<UserEntity> participants = conversationSpec.getParticipantIds().stream()
                .map(this::mapParticipantIdToUserEntity)
                .collect(toList());
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
                .collect(toList());
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
