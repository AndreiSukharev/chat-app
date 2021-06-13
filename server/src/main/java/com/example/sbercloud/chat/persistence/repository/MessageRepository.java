package com.example.sbercloud.chat.persistence.repository;

import com.example.sbercloud.chat.persistence.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
public interface MessageRepository extends PagingAndSortingRepository<MessageEntity, Long> {

    Page<MessageEntity> findAllByConversation_Id(Pageable pageRequest, long conversation);

}
