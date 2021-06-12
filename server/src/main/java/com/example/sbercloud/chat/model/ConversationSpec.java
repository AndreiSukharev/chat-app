package com.example.sbercloud.chat.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Спецификация описывающая беседу.
 *
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
public class ConversationSpec {

    private List<Long> participantIds = new ArrayList<>();
}
