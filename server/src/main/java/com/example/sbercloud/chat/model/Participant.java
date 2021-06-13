package com.example.sbercloud.chat.model;

import lombok.Data;

import java.util.Set;

/**
 * Участник беседы
 *
 * @author Bulygin D.N.
 * @since 13.06.2021
 */
@Data
public class Participant {

    /**
     * Идентификатор пользователя
     */
    private Long userId;

    /**
     * Разрешения участника беседы
     */
    private Set<Permission> permissions;
}
