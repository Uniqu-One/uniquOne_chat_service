package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.service.chatRoom.IChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/chat")
public class ChatController {

    private final IChatRoomService iChatRoomService;

    // todo userId 토큰 대체
    // 유저 채팅방 목록
    @GetMapping("/{userId}")
    public Flux<ChatRoomOutDto> findAllUserRoom(@PathVariable Long userId) {
        return iChatRoomService.findAllUserRoom(userId);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public Mono<String> createRoom(@RequestBody ChatRoomDto chatRoomDto) {
        return iChatRoomService.createRoom(chatRoomDto);
    }






    // 모든 채팅방 목록 반환
    @GetMapping("/test")
    public String test() {
        return "hello";
    }

    // todo 미사용
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    public Flux<ChatRoom> room() {
        return iChatRoomService.findAllRoom();
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public Mono<ChatRoom> roomInfo(@PathVariable String roomId) {
        return iChatRoomService.findRoomById(roomId);
    }


}
