package com.sparos.uniquone.msachatservice.chat.controller;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.msachatservice.chat.service.IChatService;
import com.sparos.uniquone.msachatservice.utils.SuccessCode;
import com.sparos.uniquone.msachatservice.utils.SuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/chat")
public class ChatController {

    private final IChatService iChatRoomService;
    private final IChatRepository iChatRepository;
    private final IChatRoomRepository iChatRoomRepository;

    // todo userId 토큰 대체
    // 유저 채팅방 목록
    @GetMapping("/{userId}")
    public ResponseEntity<SuccessResponse> findAllUserRoom(@PathVariable Long userId) {
        JSONObject jsonObject = iChatRoomService.findAllUserRoom(userId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, (String) jsonObject.get("result"), jsonObject.get("data")));
    }

    // 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<SuccessResponse> createRoom(@RequestBody ChatRoomDto chatRoomDto) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, iChatRoomService.createRoom(chatRoomDto)));
    }

    // 채팅방 나가기
    @PutMapping("/room")
    public ResponseEntity<SuccessResponse> exitRoom(@RequestBody ChatRoomExitDto chatRoomExitDto) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, iChatRoomService.exitRoom(chatRoomExitDto)));
    }

    // 채팅방 삭제
    @PostMapping("/room/{roomId}")
    public ResponseEntity<SuccessResponse> deleteRoom(@PathVariable String roomId) {
        log.info("roomId => {}", roomId);
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, iChatRoomService.deleteRoom(roomId)));
    }

    // todo 판매자인지 구매자인지 리턴 해 줘야함
    // 채팅 내용 가져오기
    @GetMapping("/room/all/{roomId}/{userId}")
    public ResponseEntity<SuccessResponse> findAllChat(@PathVariable String roomId, @PathVariable Long userId) {
        return ResponseEntity.ok(SuccessResponse.of(SuccessCode.SUCCESS_CODE, iChatRoomService.findAllChat(roomId, userId)));
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

    // dbTest
    @GetMapping("/dbTest2")
    public ChatRoom test2() {
        System.err.println("dbTest2");
        return iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrActorIdAndReceiverId
                (2l, true, true, 1l, 3l, 3l, 1l).get();
    }

}
