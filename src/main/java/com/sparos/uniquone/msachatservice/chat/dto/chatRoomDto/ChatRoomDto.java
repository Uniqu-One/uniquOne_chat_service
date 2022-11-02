package com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto;

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
public class ChatRoomDto {

    private Long postId;

    private ChatRoomType chatType;

    private LocalDateTime regDate;

}
