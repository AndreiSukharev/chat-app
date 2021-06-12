package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.persistence.entity.UserEntity;

/**
 * Сервис для обработки пользователя после сохранения
 */
public interface UserPostSaveHandler {

    void handle(UserEntity user);

}
