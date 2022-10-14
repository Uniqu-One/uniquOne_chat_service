package com.sparos.uniquone.msachatservice.chat.service;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import org.json.JSONObject;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;

public interface IChatService {

    // 모든 채팅방 리스트
    List<ChatRoom> findAllRoom();

    // 유저 채팅방 리스트
    JSONObject findAllUserRoom(Long userId);

    // 특정 채팅방 조회
    ChatRoom findRoomById(String roomId);

    // 채팅방 생성
    Object createRoom(ChatRoomDto chatRoomDto);

    // 채팅방 나가기
    String exitRoom(ChatRoomExitDto chatRoomPutDto);

    // 채팅방 삭제
    String deleteRoom(String roomId);

    // 채팅 내용 조회
    Object findAllChat(String roomId, Long userId);

    // 채팅 전송
    Chat sendChat(ChatDto chatDto);

    // 채팅방 입장
    Object enterChatRoom(ChatDto chatDto);

    // 토픽 조회
    ChannelTopic getTopic(String roomId);

    // 토픽 전체 조회
    Set<String> getTopics();
}
