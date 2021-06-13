package com.example.sbercloud.chat.persistence.repository;

import com.example.sbercloud.chat.persistence.entity.ConversationEntity;
import com.example.sbercloud.chat.persistence.entity.ParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
public interface ParticipantRepository extends JpaRepository<ParticipantEntity, Long> {

}
