package com.sparos.uniquone.msachatservice.chat.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparos.uniquone.msachatservice.chat.enums.ChatRoomType;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "chatRoom")
public class ChatRoom implements Serializable {

    @Id
    private String id; // pk

    @Field(name = "user_id")
    private Long actorId; // 유저 id

    @Field(name = "other_user_id")
    private Long receiverId; // 받는 유저 id

    private Long postId; // 게시물 id

    private Boolean isActor; // 유저 참여 여부

    private Boolean isReceiver; // 받는 유저 참여 여부

    //    @Enumerated(EnumType.STRING)
    private ChatRoomType chatType; // 채팅 타입 (SELLER, BUYER, NORMAL)

    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Field(name = "reg_date")
    private LocalDateTime regDate;


    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setType(ChatRoomType chatType) {
        this.chatType = chatType;
    }

    public void setActor(Boolean actor) {
        isActor = actor;
    }

    public void setReceiver(Boolean receiver) {
        isReceiver = receiver;
    }
}
