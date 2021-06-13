package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.model.User;
import com.example.sbercloud.chat.model.UserSpec;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
import com.example.sbercloud.chat.service.user.UserPostSaveHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

import static java.text.MessageFormat.format;
import static java.util.stream.Collectors.toList;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * @author Bulygin D.N.
 * @since 12.06.2021
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserRestController {

    private final UserRepository userRepository;

    private final List<UserPostSaveHandler> postSaveHandlers;

    @GetMapping("/{userId}")
    public User findUser(@PathVariable long userId) {
        Optional<UserEntity> userSearchResult = userRepository.findById(userId);
        User foundUser = userSearchResult
                .map(this::mapUserEntityToUser).orElseThrow(() -> new RuntimeException(format("User is not found. User id: [{0}]", userId)));
        log.debug(format("User found: [{0}]", foundUser));
        return foundUser;
    }

    @GetMapping
    public List<User> getUsers() {
        List<User> foundUsers = StreamSupport.stream(userRepository.findAll().spliterator(), false)
                .map(this::mapUserEntityToUser)
                .collect(toList());
        log.debug(format("Found users: [{0}]", foundUsers));
        return foundUsers;
    }

    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    public void createUser(final @RequestBody UserSpec userSpec) {
        UserEntity userEntity = userRepository.findByUsername(userSpec.getUsername())
                .orElseGet(() -> createUserInternal(userSpec));
    }

    private UserEntity createUserInternal(UserSpec userSpec) {
        UserEntity userEntity = mapUserSpecToUserEntity(userSpec);
        userRepository.save(userEntity);
        postSaveHandlers.forEach(userPostSaveHandler -> userPostSaveHandler.handle(userEntity));
        return userEntity;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public void updateUser(@RequestBody User user) {
        // TODO: 12/06/2021 падаем если нет пользователя
        UserEntity currentUserEntity = userRepository.findById(user.getId()).get();
        updateUserEntity(currentUserEntity, user);
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

    private User mapUserEntityToUser(UserEntity userEntity) {
        User user = new User();
        user.setId(userEntity.getId());
        user.setFirstName(userEntity.getFirstName());
        user.setLastName(userEntity.getLastName());
        user.setUsername(userEntity.getUsername());
        user.setEmail(userEntity.getEmail());
        return user;
    }
}
