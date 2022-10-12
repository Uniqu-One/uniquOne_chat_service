package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequiredArgsConstructor
@Controller
public class RedisController {

    private final RedisPublisher redisPublisher;
    private final IChatService iChatService;

    @MessageMapping("/chat/message")
    public String message(ChatDto chatDto) {
        if (ChatDto.MessageType.ENTER.equals(chatDto.getType())) {
            iChatService.enterChatRoom(chatDto.getChatRoomId());
//            chatDto.setMessage(chatDto.getSenderId() + "님이 입장하셨습니다.");
        } else {
            redisPublisher.publish(iChatService.getTopic(chatDto.getChatRoomId()), iChatService.sendChat(chatDto));
        }

        return "ok";
    }
}
