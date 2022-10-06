package com.sparos.uniquone.msachatservice.chat.domain;

import com.sparos.uniquone.msachatservice.chat.enums.ChatRoomType;
import lombok.*;
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
    private String id;

    @Field(name = "user_id")
    private Long actorId;

    @Field(name = "other_user_id")
    private Long receiverId;

    private Long postId;

    //    @Enumerated(EnumType.STRING)
    private ChatRoomType chatType;

    @CreatedDate
    @Field(name = "reg_date")
    private LocalDateTime regDate;


    public void setReceiverId(Long receiverId) {
        this.receiverId = receiverId;
    }

    public void setType(ChatRoomType chatType) {
        this.chatType = chatType;
    }
}
