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

//    @Query("{'chatRoomId' : ?0}" )
    @Aggregation(pipeline = {
            "{ '$match': { 'chatRoomId' : ?0 } }",
            "{ '$sort' : { '_id' : -1 } }",
            "{ '$limit' : 1 }"
    })
    Optional<Chat> findOneByChatRoomId(String chatRoomId);

//    Mono<Chat> findByChatRoomId(String chatRoomId);
}

/*
SELECT * FROM student ORDER BY ROWID DESC LIMIT 1;
select
        *
        from(
        select
        *
        from tb_test
        where (code, date_time) in (
        select code, max(date_time) as date_time
        from tb_test group by code
        )
        order by date_time desc
        ) t
        group by t.code*/
