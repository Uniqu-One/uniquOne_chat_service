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
@Controller
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/chat")
public class ChatController {

    private final IChatRoomService iChatRoomService;

    // 모든 채팅방 목록 반환
    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "hello";
    }

    // todo 미사용
    // 모든 채팅방 목록 반환
    @GetMapping("/rooms")
    @ResponseBody
    public Flux<ChatRoom> room() {
        return iChatRoomService.findAllRoom();
    }

    // todo userId 토큰 대체
    // 모든 채팅방 목록 반환
    @GetMapping("/{userId}")
    @ResponseBody
    public Flux<ChatRoomOutDto> findAllUserRoom(@PathVariable Long userId) {
        return iChatRoomService.findAllUserRoom(userId);
    }


    // 채팅방 생성
    @PostMapping("/room")
    @ResponseBody
    public Mono<ChatRoom> createRoom(@RequestBody ChatRoomDto chatRoomDto) {
        return iChatRoomService.createRoom(chatRoomDto);
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    @ResponseBody
    public Mono<ChatRoom> roomInfo(@PathVariable String roomId) {
        return iChatRoomService.findRoomById(roomId);
    }


}
