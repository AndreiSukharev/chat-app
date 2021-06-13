package com.example.sbercloud.chat.persistence.repository;

import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import com.example.sbercloud.chat.persistence.entity.SimpleMessageEntity;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
public interface SimpleMessageRepository extends CrudRepository<SimpleMessageEntity, Long> {
}
