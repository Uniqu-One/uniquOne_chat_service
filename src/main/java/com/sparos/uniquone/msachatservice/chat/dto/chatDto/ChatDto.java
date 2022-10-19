package com.sparos.uniquone.msachatservice.chat.dto.chatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {
    // 메시지 타입 : 입장, 채팅
    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type; // 메시지 타입

    private String chatRoomId;
    private String message;
    private LocalDateTime regDate;


    public void setMessage(String message) {
        this.message = message;
    }
}
