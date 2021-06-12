package com.example.sbercloud.chat.dal.repository;

import com.example.sbercloud.chat.dal.entity.ConversationEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
public interface ConversationRepository extends CrudRepository<ConversationEntity, Long> {

}
