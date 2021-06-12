package com.example.sbercloud.chat.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Data
public class Conversation {

    private long id;

    private List<User> participants = new ArrayList<>();

}
