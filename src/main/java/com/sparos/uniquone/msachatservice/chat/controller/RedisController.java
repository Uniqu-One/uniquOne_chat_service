package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class RedisController {

    private final RedisPublisher redisPublisher;
    private final IChatService iChatService;

    @MessageMapping("/chat/message")
    public void message(ChatDto chatDto, @Headers Map<String, Object> headers) {

        LinkedMultiValueMap<String, List<String>> s = (LinkedMultiValueMap<String, List<String>>) headers.get("nativeHeaders");
        String token = String.valueOf(s.get("Authorization").get(0));

        if (ChatDto.MessageType.ENTER.equals(chatDto.getType())) {
            iChatService.enterChatRoom(chatDto, token);
//            chatDto.setMessage(chatDto.getSenderId() + "님이 입장하셨습니다.");
        } else {
            redisPublisher.publish(iChatService.getTopic(chatDto.getChatRoomId()), iChatService.sendChat(chatDto, token));
        }

    }
}
