package com.sparos.uniquone.msachatservice.chat.domain;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.sparos.uniquone.msachatservice.utils.enums.ChatRoomType;
import com.sparos.uniquone.msachatservice.utils.enums.ChatType;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection="chat")
public class Chat implements Serializable {

    @Id
    private String id;

    @Field(name = "user_id")
    private Long senderId;

    private String chatRoomId;

    private String message;

    private ChatType type;

    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @Field(name = "reg_date")
    private LocalDateTime regDate;

    @Builder
    public Chat(Long senderId, String chatRoomId, String message, ChatType type, LocalDateTime regDate) {
        this.senderId = senderId;
        this.chatRoomId = chatRoomId;
        this.message = message;
        this.type = type;
        this.regDate = regDate;
    }

}
