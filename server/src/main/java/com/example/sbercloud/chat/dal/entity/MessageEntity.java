package com.example.sbercloud.chat.dal.entity;

import com.example.sbercloud.chat.dal.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
@Entity(name = "message")
public class MessageEntity {

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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="sender_id")
    private UserEntity sender;

}
