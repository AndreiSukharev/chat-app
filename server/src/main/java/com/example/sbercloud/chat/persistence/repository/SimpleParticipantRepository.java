package com.example.sbercloud.chat.persistence.repository;

import com.example.sbercloud.chat.persistence.entity.ParticipantEntity;
import com.example.sbercloud.chat.persistence.entity.SimpleParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
public interface SimpleParticipantRepository extends JpaRepository<SimpleParticipantEntity, Long> {

}
