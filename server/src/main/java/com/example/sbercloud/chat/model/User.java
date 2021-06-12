package com.example.sbercloud.chat.model;

import lombok.Data;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
public class User {

    /**
     * Идентификатор
     */
    private Long id;

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
