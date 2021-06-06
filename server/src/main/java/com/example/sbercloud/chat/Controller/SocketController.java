package com.example.sbercloud.chat.Controller;

import com.example.sbercloud.chat.model.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class SocketController {

    @MessageMapping("/sendMessage")
    @SendTo("/topic/getMessage")
    public Message send(@Payload Message message) {
        System.out.println(message);
        return message;
    }
}