package com.example.sbercloud.chat.model;

import lombok.Data;

/**
 * Спецификация описывающая пользователя.
 *
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
public class UserSpec {

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
