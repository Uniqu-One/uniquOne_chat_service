package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/chat")
public class ChatController {

    private final IChatService iChatRoomService;
    private final IChatRepository iChatRepository;

    // todo userId 토큰 대체
    // 유저 채팅방 목록
    @GetMapping("/{userId}")
    public List<ChatRoomOutDto> findAllUserRoom(@PathVariable Long userId) {
        return iChatRoomService.findAllUserRoom(userId);
    }

    // 채팅방 생성
    @PostMapping("/room")
    public String createRoom(@RequestBody ChatRoomDto chatRoomDto) {
        return iChatRoomService.createRoom(chatRoomDto);
    }

    // 채팅방 나가기
    @PutMapping("/room")
    public String exitRoom(@RequestBody ChatRoomExitDto chatRoomExitDto) {
        return iChatRoomService.exitRoom(chatRoomExitDto);
    }

    // 채팅 내용 가져오기
    @GetMapping("/room/all/{roomId}/{userId}")
    public Object findAllChat(@PathVariable String roomId, @PathVariable Long userId) {
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
    public List<ChatRoom> room() {
        return iChatRoomService.findAllRoom();
    }

    // 특정 채팅방 조회
    @GetMapping("/room/{roomId}")
    public ChatRoom roomInfo(@PathVariable String roomId) {
        return iChatRoomService.findRoomById(roomId);
    }




    // 특정 채팅방 조회
    @GetMapping("/dbTest/{roomId}")
    public Chat test(@PathVariable String roomId) {
        System.err.println("dbTest");
        return iChatRepository.findOneByChatRoomId(roomId);
    }


}
