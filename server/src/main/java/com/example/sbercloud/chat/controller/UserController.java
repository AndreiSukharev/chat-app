package com.example.sbercloud.chat.controller;

import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import com.example.sbercloud.chat.model.User;
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
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/{userId}")
    public UserEntity getUser(@PathVariable long userId) {
        Optional<UserEntity> byId = userRepository.findById(userId);
        return byId.get();
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public void createUser(@RequestBody User user) {
        UserEntity userEntity = mapUserToUserEntity(user);
        UserEntity save = userRepository.save(userEntity);
    }

    private UserEntity mapUserToUserEntity(User user) {
        UserEntity userEntity = new UserEntity();
        userEntity.setFirstName(user.getFirstName());
        userEntity.setLastName(user.getLastName());
        userEntity.setUsername(user.getUsername());
        userEntity.setEmail(user.getEmail());
        return userEntity;
    }
}
