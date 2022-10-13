package com.sparos.uniquone.msachatservice.chat.repository;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Repository
public interface IChatRepository extends MongoRepository<Chat, String> {

    List<Chat> findByChatRoomId(String chatRoomId);

    @Aggregation(pipeline = {
            "{ '$match': { 'chatRoomId' : ?0 } }",
            "{ '$sort' : { '_id' : -1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<Chat> findOneByChatRoomId(String chatRoomId);

}
