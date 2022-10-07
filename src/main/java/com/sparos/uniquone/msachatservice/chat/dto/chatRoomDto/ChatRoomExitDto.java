package com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomExitDto {

    private Long userId;
    private String chatRoomId;


}