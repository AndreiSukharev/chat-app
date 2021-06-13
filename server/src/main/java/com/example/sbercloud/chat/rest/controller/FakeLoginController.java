package com.example.sbercloud.chat.rest.controller;

import com.example.sbercloud.chat.model.User;
import com.example.sbercloud.chat.model.UserSpec;
import com.example.sbercloud.chat.persistence.entity.UserEntity;
import com.example.sbercloud.chat.persistence.repository.UserRepository;
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
@RequestMapping("/fakeLogin")
public class FakeLoginController {

    private final UserRestController userRestController;

    @GetMapping("/{userName}")
    public void findUser(@PathVariable String userName) {
        UserSpec user = new UserSpec();
        user.setUsername(userName);
        userRestController.createUser(user);
    }

}
