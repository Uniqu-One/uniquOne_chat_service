package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/chat")
public class ChatController {

    private final IChatService iChatRoomService;

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

    // 채팅방 나가기
    @PutMapping("/room")
    public Mono<String> exitRoom(@RequestBody ChatRoomExitDto chatRoomExitDto) {
        return iChatRoomService.exitRoom(chatRoomExitDto);
    }

    // 채팅 내용 가져오기
    @GetMapping("/room/all/{roomId}/{userId}")
    public Mono<Object> findAllChat(@PathVariable String roomId, @PathVariable Long userId) {
        return iChatRoomService.findAllChat(roomId, userId);
    }


    // test
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
