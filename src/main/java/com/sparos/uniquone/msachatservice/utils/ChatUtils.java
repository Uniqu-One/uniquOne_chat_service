package com.sparos.uniquone.msachatservice.utils;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatResponseDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.outband.post.dto.PostResponseDto;
import com.sparos.uniquone.msachatservice.outband.user.dto.UserResponseDto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class ChatUtils {
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
                .postType(postResponseDto.getPostType())
                .postImg(postResponseDto.getPostImg())
                .message(chat.getMessage())
                .msgRegDate(converter(chat.getRegDate()))
                .build();
    }

    public static ChatOutDto entityToChatOutDto(ChatRoom chatRoom, UserResponseDto userResponseDto, PostResponseDto postResponseDto, List<Chat> chats) {

        List<ChatResponseDto> chatResponseDtos = new ArrayList<>();

        if (chats != null){
            for (Chat chat : chats) {
                chatResponseDtos.add(ChatUtils.entityToChatResponseDto(chat));
            }
        } else {
            chatResponseDtos = null;
        }

        return ChatOutDto.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomType(chatRoom.getChatType())
                .postId(postResponseDto.getPostId())
                .postTitle(postResponseDto.getPostTitle())
                .postImg(postResponseDto.getPostImg())
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

        String date = chat.getRegDate().format(DateTimeFormatter.ofPattern("yy??? MM??? dd???"));
        String time = chat.getRegDate().format(DateTimeFormatter.ofPattern("a hh:mm"));

        return ChatResponseDto.builder()
                .senderId(chat.getSenderId())
                .message(chat.getMessage())
                .date(date)
                .regTime(time)
                .build();
    }

    public static String converter(LocalDateTime msgRegDate) {

        LocalDateTime now = LocalDateTime.now();
        Long diffTime = 0l;

        if (msgRegDate != null)
            diffTime = msgRegDate.until(now, ChronoUnit.SECONDS);

        String msg = null;

        if (diffTime < SEC) {
            // sec
            msg = diffTime + "??? ???";
        } else if ((diffTime /= SEC) < MIN) {
            // min
            msg = diffTime + "??? ???";
        } else if ((diffTime /= MIN) < HOUR) {
            // hour
            msg = (diffTime) + "?????? ???";
        } else if ((diffTime /= HOUR) < DAY) {
            // day
            msg = (diffTime) + "??? ???";
        } else if ((diffTime /= DAY) < MONTH) {
            // day
            msg = (diffTime) + "??? ???";
        } else {
            msg = (diffTime) + "??? ???";
        }
        return msg;

    }

}
