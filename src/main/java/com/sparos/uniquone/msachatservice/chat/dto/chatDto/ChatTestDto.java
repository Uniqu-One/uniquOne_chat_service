package com.sparos.uniquone.msachatservice.chat.dto.chatDto;

import com.sparos.uniquone.msachatservice.utils.enums.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatTestDto {

    private String chatId;

    private Long senderId;
    private String message;
    private LocalDateTime regDate;

    private String chatRoomId;

    private Long actorId;
    private Long receiverId;
    private Long postId;

    private Boolean isActor;
    private Boolean isReceiver;

    private ChatRoomType chatType;

    private LocalDateTime roomRegDate;


}
