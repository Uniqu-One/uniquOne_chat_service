package com.sparos.uniquone.msachatservice.utils;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.outband.post.dto.PostResponseDto;
import com.sparos.uniquone.msachatservice.outband.user.dto.UserResponseDto;

public class ChatRoomUtils {

    public static ChatRoomOutDto entityToChatRoomOutDto(ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto) {
        // todo
        //  최근 chat 처리
        return ChatRoomOutDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatType(chatRoom.getChatType())
                .receiverId(userResponseDto.getUserId())
                .receiverName(userResponseDto.getNickname())
                .cornImg(postResponseDto.getCornImg())
                .postId(postResponseDto.getPostId())
                .postImg(postResponseDto.getPostImg())
                .message("최근 메시지")
                .regDate("최근 메시지 시간")
                .build();
    }

    public static ChatRoom chatRoomDtoToEntity(ChatRoomDto chatRoomDto) {
        return ChatRoom.builder()
                .actorId(chatRoomDto.getActorId())
                .receiverId(chatRoomDto.getReceiverId())
                .postId(chatRoomDto.getPostId())
                .chatType(chatRoomDto.getChatType())
                .build();
    }

  /*  public static ChatRoomOutDto entityToChatRoomOutDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .userId(chatRoom.getActorId())
                .receiverId(chatRoom.getOtherUserId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .build();
    }
    */

    /*public static ChatRoomDto entityToChatRoomDto(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .roomId(chatRoom.getId())
                .userId(chatRoom.getUserId())
                .otherUserId(chatRoom.getOtherUserId())
                .name(chatRoom.getName())
                .type(chatRoom.getType())
                .build();
    }*/

}
