package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.model.Conversation;
import com.example.sbercloud.chat.model.ConversationSpec;
import com.example.sbercloud.chat.model.Participant;
import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.ParticipantEntity;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.ConversationRepository;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.StreamSupport;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

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
                .filter(conversationEntity -> isParticipant(conversationEntity.getParticipants(), userId))
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

    private boolean isParticipant(Set<ParticipantEntity> participantEntities, long userId) {
        return participantEntities.stream().anyMatch(participantEntity -> participantEntity.getUser().getId() == userId);
    }

    private ConversationEntity mapConversationSpecToConversationEntity(ConversationSpec conversationSpec) {
        ConversationEntity conversationEntity = new ConversationEntity();
        conversationEntity.setTitle(conversationSpec.getTitle());
        conversationEntity.setType(conversationSpec.getType());
        Set<ParticipantEntity> participants = conversationSpec.getParticipants().stream()
                .map(this::mapParticipantToParticipantEntity)
                .collect(toSet());
        conversationEntity.setParticipants(participants);
        return conversationEntity;
    }

    private Conversation mapConversationEntityToConversation(ConversationEntity conversationEntity) {
        Conversation conversation = new Conversation();
        conversation.setId(conversationEntity.getId());
        conversation.setTitle(conversationEntity.getTitle());
        conversation.setType(conversationEntity.getType());
        Set<Participant> participants = conversationEntity.getParticipants().stream()
                .map(this::mapParticipantEntityToParticipant)
                .collect(toSet());
        conversation.setParticipants(participants);
        return conversation;
    }

    private Participant mapParticipantEntityToParticipant(ParticipantEntity participantEntity) {
        Participant participant = new Participant();
        participant.setUserId(participantEntity.getUser().getId());
        participant.setPermissions(participantEntity.getPermissions());
        return participant;
    }

    private ParticipantEntity mapParticipantToParticipantEntity(Participant participant) {
        ParticipantEntity participantEntity = new ParticipantEntity();
        UserEntity userEntity = findUserEntityById(participant.getUserId());
        participantEntity.setUser(userEntity);
        participantEntity.setPermissions(participant.getPermissions());
        return participantEntity;
    }

    private UserEntity findUserEntityById(long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new RuntimeException(format("User is not found. User id: [{0}]", userId)));
    }
}
