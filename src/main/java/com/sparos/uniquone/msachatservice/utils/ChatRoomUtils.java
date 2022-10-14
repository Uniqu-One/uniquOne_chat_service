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

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ChatRoomUtils {
    public static final int SEC = 60;
    public static final int MIN = 60;
    public static final int HOUR = 24;
    public static final int DAY = 30;
    public static final int MONTH = 12;

    public static ChatRoomOutDto entityToChatRoomOutDto(Chat chat, ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto) {

        return ChatRoomOutDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatType(chatRoom.getChatType())
                .receiverId(userResponseDto.getUserId())
                .receiverName(userResponseDto.getNickname())
                .isReceiver(chatRoom.getIsReceiver())
                .cornImg(postResponseDto.getCornImg())
                .postId(postResponseDto.getPostId())
                .postImg(postResponseDto.getPostImg())
                .message(chat.getMessage())
                .msgRegDate(converter(chat.getRegDate()))
//                .message("최근 메시지")
//                .regDate(null)
                .build();
    }

    public static ChatOutDto entityToChatOutDto(ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto, List<Chat> chats) {

        List<ChatResponseDto> chatResponseDtos = new ArrayList<>();
        for (Chat chat : chats) {
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

        String date = chat.getRegDate().format(DateTimeFormatter.ofPattern("yy년 MM월 dd일"));
        String time = chat.getRegDate().format(DateTimeFormatter.ofPattern("a hh:mm"));


        return ChatResponseDto.builder()
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .date(date)
                .regTime(time)
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

    public static String converter(LocalDateTime msgRegDate) {

//        LocalDateTime startDateTime = LocalDateTime.of(2022, 11, 14, 14, 59, 30);

        LocalDateTime now = LocalDateTime.now();
        Long diffTime = msgRegDate.until(now, ChronoUnit.SECONDS);

        String msg = null;

        if (diffTime < SEC) {
            // sec
            msg = diffTime + "초 전";
        } else if ((diffTime /= SEC) < MIN) {
            // min
            msg = diffTime + "분 전";
        } else if ((diffTime /= MIN) < HOUR) {
            // hour
            msg = (diffTime) + "시간 전";
        } else if ((diffTime /= HOUR) < DAY) {
            // day
            msg = (diffTime) + "일 전";
        } else if ((diffTime /= DAY) < MONTH) {
            // day
            msg = (diffTime) + "달 전";
        } else {
            msg = (diffTime) + "년 전";
        }
        return msg;

    }


}
