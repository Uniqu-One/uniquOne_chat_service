package com.sparos.uniquone.msachatservice.chat.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="chat")
public class Chat {

    @Id
    private String id;

    @Field(name = "user_id")
    private Long senderId;

    private String chatRoomId;

    private String message;

    @CreatedDate
    @Field(name = "reg_date")
    private LocalDateTime regDate;

}
