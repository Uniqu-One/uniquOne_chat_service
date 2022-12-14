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
public class ChatResponseDto {

    private Long senderId;
    private String message;
    private String date;
    private String regTime;

}
