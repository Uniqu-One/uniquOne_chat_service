package com.sparos.uniquone.msachatservice.chat.dto.chatDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import reactor.core.publisher.Flux;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ChatOutDto {

    private String chatRoomId;

    private Long postId;
    private String postDsc;
    private Long postPrice;
    private String postType; // todo enum으로 변경
    private Boolean isOffer;

    private Long receiverId;
    private String receiverName;
    private String receiverImg;

    private List<ChatResponseDto> chatResponseDtos;

}
