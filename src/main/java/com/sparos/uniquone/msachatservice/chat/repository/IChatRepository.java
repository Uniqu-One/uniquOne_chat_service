package com.sparos.uniquone.msachatservice.chat.repository;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IChatRepository extends ReactiveMongoRepository<Chat, String> {

//    Flux<Chat> findByChatRoomId(String chatRoomId);

}

