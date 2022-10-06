package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.service.chatRoom.IChatRoomService;
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
    private final IChatRoomService iChatRoomService;

    @MessageMapping("/chat/message")
    public void message(ChatDto chatDto) {
        if (ChatDto.MessageType.ENTER.equals(chatDto.getType())) {
            iChatRoomService.enterChatRoom(chatDto.getChatRoomId());
            chatDto.setMessage(chatDto.getSenderId() + "님이 입장하셨습니다.");
        }
        redisPublisher.publish(iChatRoomService.getTopic(chatDto.getChatRoomId()), chatDto);
    }
}
