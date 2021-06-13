package com.example.sbercloud.chat.model;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
public class Conversation {

    /**
     * Идентификатор беседы
     */
    private String id;

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
    private Set<Participant> participants = new HashSet<>();

}
