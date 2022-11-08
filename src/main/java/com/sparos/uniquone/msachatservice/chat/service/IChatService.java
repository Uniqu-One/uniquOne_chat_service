package com.sparos.uniquone.msachatservice.chat.service;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomCreateDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import org.json.JSONObject;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

public interface IChatService {

    // 모든 채팅방 리스트
    List<ChatRoom> findAllRoom();

    // 유저 채팅방 리스트
    JSONObject findAllUserRoom(HttpServletRequest request);

    // 특정 채팅방 조회
    ChatRoom findRoomById(String roomId);

    // 채팅방 생성
    JSONObject createRoom(ChatRoomCreateDto chatRoomCreateDto, HttpServletRequest request);

    // 채팅방 나가기
    JSONObject exitRoom(String chatRoomId, HttpServletRequest request);

    // 채팅방 삭제
    JSONObject deleteRoom(String roomId);

    // 채팅 내용 조회
    JSONObject findAllChat(String roomId, HttpServletRequest request);

    // 채팅 전송
    Chat sendChat(ChatDto chatDto, String token);

    // 채팅방 입장
    void enterChatRoom(ChatDto chatDto, String token);

    // 토픽 조회
    ChannelTopic getTopic(String roomId);

    // 토픽 전체 조회
    Set<String> getTopics();

    // offer 수락 일 때 채팅방 생성 -> chatRoomId 리턴
    void offerChat(ChatRoomDto chatRoomDto, String token);

    //
    String offerChat(Long postId, Long userId, Long receiverId);
}
