package com.example.sbercloud.chat.persistence.entity;

import com.example.sbercloud.chat.model.Permission;
import com.example.sbercloud.chat.persistence.converter.PermissionSetConverter;
import com.example.sbercloud.chat.persistence.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Set;

/**
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
@Data
@Entity(name = "participant")
public class ParticipantEntity {

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
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    /**
     * Разрешения участника беседы
     */
    @Convert(converter = PermissionSetConverter.class)
    private Set<Permission> permissions;
}
