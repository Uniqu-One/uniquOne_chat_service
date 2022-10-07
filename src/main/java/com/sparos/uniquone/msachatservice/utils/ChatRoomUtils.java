package com.sparos.uniquone.msachatservice.utils;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatResponseDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.outband.post.dto.PostResponseDto;
import com.sparos.uniquone.msachatservice.outband.user.dto.UserResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomUtils {

    public static ChatRoomOutDto entityToChatRoomOutDto(ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto) {
        // todo
        //  최근 chat 처리
        return ChatRoomOutDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatType(chatRoom.getChatType())
                .receiverId(userResponseDto.getUserId())
                .receiverName(userResponseDto.getNickname())
                .isReceiver(chatRoom.getIsReceiver())
                .cornImg(postResponseDto.getCornImg())
                .postId(postResponseDto.getPostId())
                .postImg(postResponseDto.getPostImg())
                .message("최근 메시지")
                .regDate("최근 메시지 시간")
                .build();
    }

    public static ChatOutDto entityToChatOutDto(ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto, List<Chat> chats) {

        List<ChatResponseDto> chatResponseDtos = new ArrayList<>();
        for (Chat chat : chats){
            chatResponseDtos.add(ChatRoomUtils.entityToChatResponseDto(chat));
        }

        return ChatOutDto.builder()
                .chatRoomId(chatRoom.getId())
                .postId(postResponseDto.getPostId())
                .postDsc(postResponseDto.getPostDsc())
                .postPrice(postResponseDto.getPostPrice())
                .postType(postResponseDto.getPostType())
                .isOffer(postResponseDto.getIsOffer())
                .receiverId(userResponseDto.getUserId())
                .receiverName(userResponseDto.getNickname())
                .receiverImg(postResponseDto.getCornImg())
                .chatResponseDtos(chatResponseDtos)
                .build();
    }

    public static ChatResponseDto entityToChatResponseDto(Chat chat) {
        return ChatResponseDto.builder()
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .regDate(chat.getRegDate())
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
