package com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto;

import com.sparos.uniquone.msachatservice.utils.enums.ChatRoomType;
import com.sparos.uniquone.msachatservice.utils.enums.PostType;
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
    private Boolean isReceiver; // 받는 유저 참여 여부

    private String cornImg;

    private Long postId;
    private String postImg;
    private PostType postType;

    private String message;
    private String msgRegDate;

}