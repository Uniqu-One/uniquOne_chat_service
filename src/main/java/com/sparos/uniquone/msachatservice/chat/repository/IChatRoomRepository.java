package com.sparos.uniquone.msachatservice.chat.repository;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;

@Repository
public interface IChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findByActorIdAndIsActorOrReceiverIdAndIsReceiver(Long actorId, Boolean isActor, Long receiverId, Boolean isReceiver);
}
