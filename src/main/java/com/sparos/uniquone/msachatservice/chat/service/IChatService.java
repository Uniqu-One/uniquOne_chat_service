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

public interface IChatService {

    // 모든 채팅방 리스트
    Flux<ChatRoom> findAllRoom();

    // 유저 채팅방 리스트
    Flux<ChatRoomOutDto> findAllUserRoom(Long userId);

    // 특정 채팅방 조회
    Mono<ChatRoom> findRoomById(String roomId);

    // 채팅방 생성
    Mono<String> createRoom(ChatRoomDto chatRoomDto);

    // 채팅방 나가기
    Mono<String> exitRoom(ChatRoomExitDto chatRoomPutDto);

    // 채팅방 입장
    void enterChatRoom(String roomId);

    Mono<Object> findAllChat(String roomId, Long userId);

    Mono<Chat> sendChat(ChatDto chatDto);

    // 토픽 조회
    ChannelTopic getTopic(String roomId);
}
