package com.example.sbercloud.chat.persistence.entity;

import com.example.sbercloud.chat.persistence.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(cascade = CascadeType.MERGE, orphanRemoval = true)
    @JoinColumn(name = "conversation_id")
    private List<UserEntity> participants;
}
