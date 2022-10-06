package com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto;

import com.sparos.uniquone.msachatservice.chat.enums.ChatRoomType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {

    // 나중에 없애기
    private Long actorId;
    // 나중에 없애기
    private Long receiverId;

    private Long postId;

    private ChatRoomType chatType;

}
