package com.sparos.uniquone.msachatservice.chat.service.chatRoom;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import org.springframework.data.redis.listener.ChannelTopic;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IChatRoomService {

    // 모든 채팅방 리스트
    Flux<ChatRoom> findAllRoom();

    // 유저 채팅방 리스트
    Flux<ChatRoomOutDto> findAllUserRoom(Long userId);

    // 특정 채팅방 조회
    Mono<ChatRoom> findRoomById(String roomId);

    // 채팅방 생성
    Mono<ChatRoom> createRoom(ChatRoomDto chatRoomDto);

    // 채팅방 입장
    void enterChatRoom(String roomId);

    // 토픽 조회
    ChannelTopic getTopic(String roomId);
}
