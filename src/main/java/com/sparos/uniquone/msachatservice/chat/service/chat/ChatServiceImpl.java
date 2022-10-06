/*
package com.sparos.uniquone.msachatservice.chat.service.chat;


import com.sparos.uniquone.maschatservice.chat.domain.Chat;
import com.sparos.uniquone.maschatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.maschatservice.chat.dto.chat.ChatDto;
import com.sparos.uniquone.maschatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.maschatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.maschatservice.outband.user.service.IUserConnect;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class ChatServiceImpl implements IChatService {

//    private final RedisTemplate<String, Chat> reactiveTemplate;
//    private final RedisMessageListenerContainer redisListenerContainer;

    // 채팅방의 대화 메시지를 발행하기 위한 redis topic 정보. 서버별로 채팅방에 매치되는 topic정보를 Map에 넣어 roomId로 찾을수 있도록 한다.
    private Map<String, ChannelTopic> topics;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;
    private HashOperations<String, String, String> hashOpsEnterInfo;

    private final IChatRepository iChatRepository;
    private final IChatRoomRepository iChatRoomRepository;
    private final IUserConnect iUserConnect;
//    private final PublishService publishService;
//    private final SubscribeService subscribeService;


    @Override
    public Mono<Void> chatPub(Mono<ChatDto> chatDto) {
        return null;
    }

    @Override
    public Flux<Chat> enterChatRoom(String roomId) {
        System.err.println("enterChatRoom");
//        System.err.println("enterChatRoom topic : " + topics.get(roomId));
//        redisListenerContainer.addMessageListener(subscribeService, topics.get(roomId));

        return iChatRepository.findByChatRoomId(roomId);
    }


}
*/
