package com.example.sbercloud.chat.security;

import com.example.sbercloud.chat.model.UserSpec;
import com.example.sbercloud.chat.rest.controller.UserRestController;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthenticationSuccessListener implements ApplicationListener<AuthenticationSuccessEvent> {

    private final UserRestController userRestController;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        final DefaultOAuth2User principal = (DefaultOAuth2User) event.getAuthentication().getPrincipal();
        final String login = principal.getAttributes().get("login").toString();
        UserSpec user = new UserSpec();
        user.setUsername(login);
        userRestController.createUser(user);
    }
}