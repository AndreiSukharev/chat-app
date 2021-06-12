package com.example.sbercloud.chat.dal.entity;

import com.example.sbercloud.chat.dal.sequence.SequenceNameGenerator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
@Entity(name = "user")
public class UserEntity {

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
     * Имя пользователя
     */
    private String firstName;

    /**
     * Фамилия пользователя
     */
    private String lastName;

    /**
     * Логин пользователя
     */
    private String username;

    /**
     * Email пользователя
     */
    private String email;
}
