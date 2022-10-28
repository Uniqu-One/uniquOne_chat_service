package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisPublisher;
import com.sparos.uniquone.msachatservice.outband.post.service.IPostConnect;
import com.sparos.uniquone.msachatservice.utils.enums.ChatType;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
public class RedisController {

    private final RedisPublisher redisPublisher;
    private final IChatService iChatService;
    private final IPostConnect iPostConnect;

    @MessageMapping("/chat/message")
    public void message(ChatDto chatDto, @Headers Map<String, Object> headers) {

        System.err.println("타입");
        System.err.println(chatDto.getType());
        LinkedMultiValueMap<String, List<String>> s = (LinkedMultiValueMap<String, List<String>>) headers.get("nativeHeaders");
        String token = String.valueOf(s.get("Authorization").get(0));

        if (ChatType.ENTER.equals(chatDto.getType())) {
            iChatService.enterChatRoom(chatDto, token);
//            chatDto.setMessage(chatDto.getSenderId() + "님이 입장하셨습니다.");
        } else {
            Chat chat = iChatService.sendChat(chatDto, token);
            redisPublisher.publish(iChatService.getTopic(chatDto.getChatRoomId()), chat);
        }



    }
}
