package com.sparos.uniquone.msachatservice.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import com.sparos.uniquone.msachatservice.chat.enums.ChatRoomType;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisSubscriber;
import com.sparos.uniquone.msachatservice.outband.post.service.IPostConnect;
import com.sparos.uniquone.msachatservice.outband.user.service.IUserConnect;
import com.sparos.uniquone.msachatservice.utils.ChatRoomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Log4j2
@RequiredArgsConstructor
@Transactional
@Service
public class ChatServiceImpl implements IChatService {

    private final IChatRoomRepository iChatRoomRepository;
    private final IChatRepository iChatRepository;
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
    public List<ChatRoom> findAllRoom() {
        return iChatRoomRepository.findAll();
    }

    // 유저 채팅방 조회
    @Override
    public List<ChatRoomOutDto> findAllUserRoom(Long userId) {

        List<ChatRoom> chatRoom = iChatRoomRepository.findByActorIdAndIsActorOrReceiverIdAndIsReceiver(userId, true, userId, true);
        List<ChatRoomOutDto> chatRoomOutDtos = new ArrayList<>();

        chatRoom.stream().map(chatRoom1-> {
            if (chatRoom1.getReceiverId().equals(userId)) {
                chatRoom1.setReceiverId(chatRoom1.getActorId());
                chatRoom1.setReceiver(chatRoom1.getIsActor());
                chatRoom1.setType(chatRoom1.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
            }
            return chatRoom1;
        }).map(chatRoom2 -> {
            Chat chat = iChatRepository.findOneByChatRoomId(chatRoom2.getId());
            return  chatRoomOutDtos.add(ChatRoomUtils.entityToChatRoomOutDto(
                    chat,
                    chatRoom2,
                    iUserConnect.getUserInfo(chatRoom2.getReceiverId()),
                    iPostConnect.getPostInfo(chatRoom2.getPostId(), chatRoom2.getReceiverId()))
            );

        });

        return chatRoomOutDtos;
    }

    @Override
    public ChatRoom findRoomById(String roomId) {
        return iChatRoomRepository.findById(roomId).get();
    }

    @Override
    public String createRoom(ChatRoomDto chatRoomDto) {
        // todo 중복 확인
        //  postId로 cornId 찾아서 receiverId 검색
        ChatRoom chatRoom = iChatRoomRepository.save(
                ChatRoom.builder()
                        .chatType(chatRoomDto.getChatType())
                        .actorId(chatRoomDto.getActorId())
                        .receiverId(chatRoomDto.getReceiverId())
                        .postId(chatRoomDto.getPostId())
                        .isActor(true)
                        .isReceiver(true)
                        .regDate(chatRoomDto.getRegDate())
                        .build());
        return chatRoom.getId();
    }

    @Override
    public String exitRoom(ChatRoomExitDto chatRoomDto) {

        return iChatRoomRepository.findById(chatRoomDto.getChatRoomId())
                .map(chatRoom -> {
                    if (chatRoom.getReceiverId().equals(chatRoomDto.getUserId())) {
                        chatRoom.setReceiver(false);
                    } else {
                        chatRoom.setActor(false);
                    }
                    return chatRoom;
                })
                .map(chatRoom -> iChatRoomRepository.save(chatRoom).getId()).get();
    }

    @Override
    public Object findAllChat(String roomId, Long userId) {

        List<Chat> chats = iChatRepository.findByChatRoomId(roomId);

        return iChatRoomRepository.findById(roomId)
                .map(chatRoom -> {
                    if (chatRoom.getReceiverId().equals(userId)) {
                        chatRoom.setReceiverId(chatRoom.getActorId());
                        chatRoom.setReceiver(chatRoom.getIsActor());
                        chatRoom.setType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
                    }
                    return chatRoom;
                }).map(chatRoom -> ChatRoomUtils.entityToChatOutDto(
                        chatRoom,
                        iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                        iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()),
                        chats)
                );
    }

    @Override
    public Chat sendChat(ChatDto chatDto) {

        return iChatRepository.save(
                        Chat.builder()
                                .senderId(chatDto.getSenderId())
                                .chatRoomId(chatDto.getChatRoomId())
                                .message(chatDto.getMessage())
                                .build());
    }

    @Override
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null) {
            topic = new ChannelTopic(roomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(roomId, topic);
        }
    }


/*
    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }
*/


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


    public Chat findOneByChatRoomId(String roomId) {
        return iChatRepository.findOneByChatRoomId(roomId);
    }
}
