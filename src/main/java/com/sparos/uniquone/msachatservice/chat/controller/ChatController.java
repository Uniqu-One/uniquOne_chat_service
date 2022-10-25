package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import com.sparos.uniquone.msachatservice.utils.response.SuccessCode;
import com.sparos.uniquone.msachatservice.utils.response.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final IChatService iChatRoomService;
    private final IChatRepository iChatRepository;
    private final IChatRoomRepository iChatRoomRepository;

    // 유저 채팅방 목록
    @GetMapping("")
    public ResponseEntity<SuccessResponse> findAllUserRoom(HttpServletRequest request) {
        JSONObject jsonObject = iChatRoomService.findAllUserRoom(request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, jsonObject.get("data")));
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<SuccessResponse> createRoom(@RequestBody ChatRoomDto chatRoomDto, HttpServletRequest request) {
        JSONObject jsonObject = iChatRoomService.createRoom(chatRoomDto, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, jsonObject.get("data")));
    }

    // 채팅방 나가기
    @PatchMapping("/room")
    public ResponseEntity<SuccessResponse> exitRoom(@RequestBody Map<String,String> chatRoomId, HttpServletRequest request) {
        JSONObject jsonObject = iChatRoomService.exitRoom(chatRoomId.get("chatRoomId"), request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, jsonObject.get("data")));
    }

    // 채팅방 삭제
    @PostMapping("/room/{roomId}")
    public ResponseEntity<SuccessResponse> deleteRoom(@PathVariable String roomId) {
        JSONObject jsonObject = iChatRoomService.deleteRoom(roomId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, jsonObject.get("data")));
    }

    // 채팅 내용 가져오기
    @GetMapping("/room/all/{roomId}")
    public ResponseEntity<SuccessResponse> findAllChat(@PathVariable String roomId, HttpServletRequest request) {
        JSONObject jsonObject = iChatRoomService.findAllChat(roomId, request);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, jsonObject.get("data")));
    }

    // 토픽 조회
    @GetMapping("/topic")
    public ResponseEntity<SuccessResponse> getTopics() {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, iChatRoomService.getTopics()));
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
        System.err.println("조회");
        return iChatRoomService.findRoomById(roomId);
    }

    // dbTest
    @GetMapping("/dbTest/{roomId}")
    public Chat test(@PathVariable String roomId) {
        System.err.println("dbTest");
        return iChatRepository.findOneByChatRoomId(roomId).get();
    }

}
