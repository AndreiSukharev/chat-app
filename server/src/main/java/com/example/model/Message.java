package com.example.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Message {

    private String text;

    private String author;

    private Date createdAt;

    @JsonCreator
    public Message(@JsonProperty("text") String text, @JsonProperty("author") String author, @JsonProperty("createdAt")Date createdAt) {
        this.text = text;
        this.author = author;
        this.createdAt = createdAt;
    }

}
