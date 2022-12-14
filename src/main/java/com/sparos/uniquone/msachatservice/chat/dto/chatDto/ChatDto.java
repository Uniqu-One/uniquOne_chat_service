package com.sparos.uniquone.msachatservice.chat.dto.chatDto;

import com.sparos.uniquone.msachatservice.utils.enums.ChatType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatDto {

    private ChatType type; // 메시지 타입

    private Long senderId;
    private String chatRoomId;
    private String message;
    private LocalDateTime regDate;
    private String date;
    private String regTime;

    public void setMessage(String message) {
        this.message = message;
    }

    public void setSenderId(Long senderId) {
        this.senderId = senderId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setRegTime(String regTime) {
        this.regTime = regTime;
    }

}
