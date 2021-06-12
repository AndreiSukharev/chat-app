package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.dal.entity.UserEntity;
import com.example.sbercloud.chat.dal.repository.UserRepository;
import com.example.sbercloud.chat.model.User;
import com.example.sbercloud.chat.model.UserSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserRestController {

    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public UserEntity getUser(@PathVariable long userId) {
        Optional<UserEntity> byId = userRepository.findById(userId);
        // TODO: 12/06/2021 тут упадём если нет пользователя
        return byId.get();
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public void createUser(@RequestBody UserSpec userSpec) {
        UserEntity userEntity = mapUserSpecToUserEntity(userSpec);
        UserEntity save = userRepository.save(userEntity);
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void updateUser(@RequestBody User user) {
        // TODO: 12/06/2021 падаем если нет пользователя
        UserEntity currentUserEntity = userRepository.findById(user.getId()).get();
        updateUserEntity(currentUserEntity, user);
        UserEntity save = userRepository.save(currentUserEntity);
    }

    private void updateUserEntity(UserEntity currentUserEntity, User user) {
        currentUserEntity.setFirstName(user.getFirstName());
        currentUserEntity.setLastName(user.getLastName());
        currentUserEntity.setUsername(user.getUsername());
        currentUserEntity.setEmail(user.getEmail());
    }

    private UserEntity mapUserSpecToUserEntity(UserSpec userSpec) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(userSpec.getFirstName());
        userEntity.setLastName(userSpec.getLastName());
        userEntity.setUsername(userSpec.getUsername());
        userEntity.setEmail(userSpec.getEmail());
        return userEntity;
    }
}
