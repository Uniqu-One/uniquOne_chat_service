package com.sparos.uniquone.msachatservice.chat.repository;

import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Optional;

@Repository
public interface IChatRoomRepository extends MongoRepository<ChatRoom, String> {

    List<ChatRoom> findByActorIdAndIsActorOrReceiverIdAndIsReceiver(Long actorId, Boolean isActor, Long receiverId, Boolean isReceiver);

    Optional<ChatRoom> findByIdAndActorIdOrIdAndReceiverId(String chatRoomId, Long actorId, String chatRoomId2, Long receiverId);

    Boolean existsByActorIdOrReceiverId(Long actorId, Long receiverId);

    String findOneIdByPostId(Long postId);

    Optional<ChatRoom> findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrActorIdAndReceiverId
            (Long postId, Boolean isActor, Boolean isReceiver, Long actorId, Long receiverId, Long actorId2, Long receiverId2);

//    ActorIdAndIsActorOrReceiverIdAndIsReceiver(Long actorId, Boolean isActor, Long receiverId, Boolean isReceiver);
}
