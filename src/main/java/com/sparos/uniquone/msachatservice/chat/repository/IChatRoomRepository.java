package com.sparos.uniquone.msachatservice.chat.repository;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IChatRoomRepository extends ReactiveMongoRepository<ChatRoom, String> {

//    Flux<ChatRoom> findAll();

//    Flux<ChatRoom> findByUserId(Long userId);

    Flux<ChatRoom> findByActorIdAndIsActorOrReceiverIdAndIsReceiver(Long actorId, Boolean isActor, Long receiverId, Boolean isReceiver);

//    Flux<ChatRoom> findByIdActorIdAndIsActorOrReceiverIdAndIsReceiver(Long actorId, Boolean isActor, Long receiverId, Boolean isReceiver);
}
