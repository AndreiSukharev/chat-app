package com.example.sbercloud.chat.persistence.entity;

import com.example.sbercloud.chat.persistence.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
@Entity(name = "simplemessage")
@Table(name = "message")
public class SimpleMessageEntity {

    /**
     * Идентификатор
     */
    @GenericGenerator(
            name = SequenceNameGenerator.GENERATOR_NAME,
            strategy = SequenceNameGenerator.STRATEGY_NAME
    )
    @Id
    @GeneratedValue(generator = SequenceNameGenerator.GENERATOR_NAME)
    private long id;

    /**
     * Контент сообщения
     */
    private String content;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "conversation_id")
    private Long conversationId;

}
