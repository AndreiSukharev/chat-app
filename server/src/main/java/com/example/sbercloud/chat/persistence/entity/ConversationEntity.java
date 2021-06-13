package com.example.sbercloud.chat.persistence.entity;

import com.example.sbercloud.chat.model.ConversationType;
import com.example.sbercloud.chat.persistence.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
@Entity(name = "conversation")
public class ConversationEntity {

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
     * Наименование беседы
     */
    private String title;

    /**
     * Тип беседы
     */
    private ConversationType type;

    /**
     * Участники беседы
     */
    @OneToMany(mappedBy = "conversation")
    private Set<ParticipantEntity> participants;
}
