package com.sparos.uniquone.msachatservice.chat.service.chatRoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.enums.ChatRoomType;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisSubscriber;
import com.sparos.uniquone.msachatservice.outband.post.service.IPostConnect;
import com.sparos.uniquone.msachatservice.outband.user.dto.UserResponseDto;
import com.sparos.uniquone.msachatservice.outband.user.service.IUserConnect;
import com.sparos.uniquone.msachatservice.utils.ChatRoomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class ChatRoomServiceImpl implements IChatRoomService {

    private final IChatRoomRepository iChatRoomRepository;
    private final IUserConnect iUserConnect;
    private final IPostConnect iPostConnect;

    private final RedisTemplate<String, Object> reactiveTemplate;
    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    private Map<String, ChannelTopic> topics;
    private final ObjectMapper objectMapper;

    @PostConstruct
    private void init() {
        topics = new HashMap<>();
    }

    // 모든 채팅방 조회
    @Override
    public Flux<ChatRoom> findAllRoom() {
        return iChatRoomRepository.findAll();
    }

    // 유저 채팅방 조회
    @Override
    public Flux<ChatRoomOutDto> findAllUserRoom(Long userId) {
        return iChatRoomRepository.findByActorIdOrReceiverId(userId, userId)
//                .map(ChatRoomUtils::entityToChatRoomOutDto)
                .map(chatRoom -> {
                    if (chatRoom.getReceiverId().equals(userId)) {
                        chatRoom.setReceiverId(chatRoom.getActorId());
                        chatRoom.setType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
                    }
                    return chatRoom;
                })
                .map(chatRoom -> ChatRoomUtils.entityToChatRoomOutDto(
                        chatRoom,
                        iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                        iPostConnect.getPostInfo(chatRoom.getPostId())
                        ));
    }

    @Override
    public Mono<ChatRoom> findRoomById(String roomId) {
        return iChatRoomRepository.findById(roomId);
    }

    @Override
    public Mono<ChatRoom> createRoom(ChatRoomDto chatRoomDto) {

        return iChatRoomRepository.save(
                ChatRoom.builder()
                        .chatType(chatRoomDto.getChatType())
                        .actorId(chatRoomDto.getActorId())
                        .receiverId(chatRoomDto.getReceiverId())
                        .postId(chatRoomDto.getPostId())
                        .build());
    }

    @Override
    public void enterChatRoom(String roomId){
        ChannelTopic topic = topics.get(roomId);
        if(topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }


    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }













    /*// todo 삭제하기 노필요
    // 전체 채팅방 리스트
    @Override
    public Flux<ChatRoom> findAllRoom() {
        return iChatRoomRepository.findAll().map(chatRoom ->
                ChatRoomUtils.entityToChatRoomOutDto(
                        chatRoom,
                        iUserConnect.getUserInfo(chatRoom.getReceiverId())));
    }

    @Override
    public Object findAllUserRoom(Long userId) {
        return iChatRoomRepository.findByActorIdOrReceiverId(userId, userId)
//                .map(ChatRoomUtils::entityToChatRoomOutDto)
                .map(chatRoom -> {
                    if (chatRoom.getReceiverId().equals(userId)) {
                        chatRoom.setReceiverId(chatRoom.getActorId());
                        chatRoom.setType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
                    }
                    return chatRoom;
                })
                .map(chatRoom -> ChatRoomUtils.entityToChatRoomOutDto(
                        chatRoom, iUserConnect.getUserInfo(chatRoom.getReceiverId())));
    }

    @Override
    public Object createRoom(ChatRoomDto chatRoomDto) {
        // todo : user와 post로 이미 존재하는 채팅방인지 확인
        //  존재 하면 바로 입장
        //  존재 하지 않으면 생성 후 입장

        return Mono.just(chatRoomDto).map(ChatRoomUtils::chatRoomDtoToEntity)
                .flatMap(iChatRoomRepository::insert)
                .doOnNext(savedChatRoom -> {
                    *//*opsHashChatRoom.put("CHAT_ROOMS", savedChatRoom.getId(), savedChatRoom);
                    topics.put(savedChatRoom.getId(), new ChannelTopic(savedChatRoom.getId()));*//*
                    ChatRoomUtils.entityToChatRoomOutDto(
                            savedChatRoom, iUserConnect.getUserInfo(savedChatRoom.getReceiverId()));
                });
    }*/



    @Override
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }



}
