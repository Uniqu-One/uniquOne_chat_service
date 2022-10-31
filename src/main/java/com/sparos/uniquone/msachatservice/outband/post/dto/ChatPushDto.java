package com.sparos.uniquone.msachatservice.outband.post.dto;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.utils.enums.PostType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatPushDto {

    private Long receiverId;
    private Long postId;
    private Chat chat;

}
