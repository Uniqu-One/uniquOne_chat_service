package com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto;

import com.sparos.uniquone.msachatservice.enums.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomOutDto {

    private String chatRoomId;
    private ChatRoomType chatType;

    private Long receiverId;
    private String receiverName;
    private String receiverImg;

    private Long postId;
    private String postImg;

    private String message;
    private String regDate;


}