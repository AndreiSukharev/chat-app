package com.example.sbercloud.chat.persistence.entity;

import com.example.sbercloud.chat.model.Permission;
import com.example.sbercloud.chat.persistence.converter.PermissionSetConverter;
import com.example.sbercloud.chat.persistence.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
@Data
@Entity(name = "simpleparticipant")
@Table(name = "participant")
public class SimpleParticipantEntity {

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
     * Пользователь
     */
    @Column(name = "user_id")
    private long userId;

    /**
     * Пользователь
     */
    private long conversationId;


    /**
     * Разрешения участника беседы
     */
    @Convert(converter = PermissionSetConverter.class)
    private Set<Permission> permissions = new HashSet<>();
}
