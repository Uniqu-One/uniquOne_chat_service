package com.sparos.uniquone.msachatservice.chat.service;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IChatService {

    // 모든 채팅방 리스트
    List<ChatRoom> findAllRoom();

    // 유저 채팅방 리스트
    Object findAllUserRoom(Long userId);

    // 특정 채팅방 조회
    ChatRoom findRoomById(String roomId);

    // 채팅방 생성
    Object createRoom(ChatRoomDto chatRoomDto);

    // 채팅방 나가기
    String exitRoom(ChatRoomExitDto chatRoomPutDto);

    // 채팅방 입장
    void enterChatRoom(String roomId);

    Object findAllChat(String roomId, Long userId);

    Chat sendChat(ChatDto chatDto);

    // 토픽 조회
    ChannelTopic getTopic(String roomId);
}
