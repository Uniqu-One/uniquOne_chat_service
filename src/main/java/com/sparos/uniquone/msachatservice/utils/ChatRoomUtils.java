package com.sparos.uniquone.msachatservice.utils;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatResponseDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatTestDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.outband.post.dto.PostResponseDto;
import com.sparos.uniquone.msachatservice.outband.user.dto.UserResponseDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class ChatRoomUtils {

    public static ChatRoomOutDto entityToChatRoomOutDto(Chat chat, ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto) {
        // todo
        //  최근 chat 처리
        return ChatRoomOutDto.builder()
//                .chatRoomId(chatRoom.getChatRoomId())
                .chatRoomId(chatRoom.getId())
                .chatType(chatRoom.getChatType())
                .receiverId(userResponseDto.getUserId())
                .receiverName(userResponseDto.getNickname())
                .isReceiver(chatRoom.getIsReceiver())
                .cornImg(postResponseDto.getCornImg())
                .postId(postResponseDto.getPostId())
                .postImg(postResponseDto.getPostImg())
                .message(chat.getMessage())
                .regDate(chat.getRegDate())
//                .message("최근 메시지")
//                .regDate(null)
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

    public static ChatRoom objectToChatRoom(ChatRoom chatRoom) {

        return ChatRoom.builder()
                .actorId(chatRoom.getActorId())
                .receiverId(chatRoom.getReceiverId())
                .postId(chatRoom.getPostId())
                .isActor(chatRoom.getIsActor())
                .isReceiver(chatRoom.getIsReceiver())
                .chatType(chatRoom.getChatType())
                .regDate(chatRoom.getRegDate())
                .build();
    }


    public static Chat objectToChat(Chat chat) {

        return Chat.builder()
                .id(chat.getId())
                .senderId(chat.getSenderId())
                .chatRoomId(chat.getChatRoomId())
                .message(chat.getMessage())
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

    public static ChatTestDto test(Chat chat, ChatRoom chatRoom) {
        return ChatTestDto.builder()
                .chatId(chat.getId())
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .regDate(chat.getRegDate())
                .chatRoomId(chatRoom.getId())
                .actorId(chatRoom.getActorId())
                .receiverId(chatRoom.getReceiverId())
                .postId(chatRoom.getPostId())
                .isActor(chatRoom.getIsActor())
                .isReceiver(chatRoom.getIsReceiver())
                .chatType(chatRoom.getChatType())
                .roomRegDate(chatRoom.getRegDate())
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
