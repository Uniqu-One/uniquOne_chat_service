package com.sparos.uniquone.msachatservice.chat.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomExitDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.chat.enums.ChatRoomType;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisSubscriber;
import com.sparos.uniquone.msachatservice.outband.post.service.IPostConnect;
import com.sparos.uniquone.msachatservice.outband.user.service.IUserConnect;
import com.sparos.uniquone.msachatservice.utils.ChatRoomUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.*;

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
    public JSONObject findAllUserRoom(Long userId) {

        List<ChatRoom> chatRooms = iChatRoomRepository.findByActorIdAndIsActorOrReceiverIdAndIsReceiver(userId, true, userId, true);
        JSONObject jsonObject = new JSONObject();

        if (chatRooms.isEmpty()){
//            return "채팅방이 존재하지 않습니다.";
            jsonObject.put("result", "채팅방이 존재하지 않습니다.");
            jsonObject.put("data", 0);
        }else {

            List<ChatRoomOutDto> chatRoomOutDtos = new ArrayList<>();

            chatRooms.forEach(chatRoom-> {

                if (chatRoom.getReceiverId().equals(userId)) {
                    chatRoom.setReceiverId(chatRoom.getActorId());
                    chatRoom.setReceiver(chatRoom.getIsActor());
                    chatRoom.setType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
                }

                Optional<Chat> chat = iChatRepository.findOneByChatRoomId(chatRoom.getId());

                if (chat.isPresent()) {
                    chatRoomOutDtos.add(ChatRoomUtils.entityToChatRoomOutDto(
                            chat.get(),
                            chatRoom,
                            iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                            iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()))
                    );
                }else { // todo 지울 코드
                    chatRoomOutDtos.add(ChatRoomUtils.entityToChatRoomOutDto(
                            Chat.builder().message("최근메시지").regDate(LocalDateTime.now()).build(),
                            chatRoom,
                            iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                            iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()))
                    );
                }
            });
            jsonObject.put("result", "채팅방 조회를 완료했습니다.");
            jsonObject.put("data", chatRoomOutDtos.toArray());
//            return chatRoomOutDtos;
        }
        return jsonObject;
    }

    // 채팅방 생성
    @Override
    public Object createRoom(ChatRoomDto chatRoomDto) {

        Optional<ChatRoom> existChatRoom =
                iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrActorIdAndReceiverId
                        (chatRoomDto.getPostId(), true, true,
                                chatRoomDto.getActorId(), chatRoomDto.getReceiverId(),
                                chatRoomDto.getReceiverId(), chatRoomDto.getActorId());

        Map<String,String> chatRoomId = new HashMap<>();
        Boolean existPost = false;

        if (existChatRoom.isPresent()){

            chatRoomId.put("chatRoomId", existChatRoom.get().getId());

        } else {

            if (chatRoomDto.getChatType().equals(ChatRoomType.BUYER)){
                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), chatRoomDto.getReceiverId());
            } else if (chatRoomDto.getChatType().equals(ChatRoomType.SELLER)) {
                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), chatRoomDto.getActorId());
            }

            if (existPost.equals(true)){
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
                chatRoomId.put("chatRoomId", chatRoom.getId());
            }else {
                return "게시물 소유자가 다릅니다.";
            }
        }
        return chatRoomId;
    }

    // 채팅방 나가기
    @Override
    public String exitRoom(ChatRoomExitDto chatRoomExitDto) {

        Optional<ChatRoom> chatRoom = iChatRoomRepository.findById(chatRoomExitDto.getChatRoomId());

        if (chatRoom.isPresent()){
            if (chatRoom.get().getReceiverId().equals(chatRoomExitDto.getUserId())) {
                chatRoom.get().setReceiver(false);
            } else {
                chatRoom.get().setActor(false);
            }
            iChatRoomRepository.save(chatRoom.get());
            return "채팅방에서 나갔습니다.";
        }
        return "채팅방이 존재하지 않습니다.";
    }

    // 채팅방 삭제
    @Override
    public String deleteRoom(String roomId) {

        Optional<ChatRoom> chatRoom = iChatRoomRepository.findById(roomId);
        if (chatRoom.isPresent()){
            iChatRoomRepository.delete(chatRoom.get());
            return "채팅방을 삭제했습니다.";
        }
        return "채팅방이 존재하지 않습니다.";
    }

    // 채팅 내용 조회
    @Override
    public Object findAllChat(String roomId, Long userId) {

        Optional<ChatRoom> chatRoomOptional = iChatRoomRepository.findByIdAndActorIdOrIdAndReceiverId(roomId, userId, roomId, userId);

        if (chatRoomOptional.isPresent()){

            List<Chat> chats = iChatRepository.findByChatRoomId(roomId);
            ChatRoom chatRoom = chatRoomOptional.get();

            if (chats.isEmpty()){
                return "채팅 내용이 없습니다.";
            }
            if (chatRoom.getReceiverId().equals(userId)) {
                chatRoom.setReceiverId(chatRoom.getActorId());
                chatRoom.setReceiver(chatRoom.getIsActor());
                chatRoom.setType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
            }

            return ChatRoomUtils.entityToChatOutDto(
                    chatRoom,
                    iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                    iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()),
                    chats);
        }

        return "채팅방이 존재하지 않거나 참여자가 아닙니다.";
    }

    // 채팅 전송
    @Override
    public Chat sendChat(ChatDto chatDto) {
        // todo exception return
        ChatRoom chatRoom = iChatRoomRepository.findByIdAndActorIdOrIdAndReceiverId(chatDto.getChatRoomId(), chatDto.getSenderId(), chatDto.getChatRoomId(), chatDto.getSenderId())
                .orElseThrow();

        return iChatRepository.save(
                Chat.builder()
                        .senderId(chatDto.getSenderId())
                        .chatRoomId(chatDto.getChatRoomId())
                        .message(chatDto.getMessage())
                        .regDate(chatDto.getRegDate())
                        .build());
    }

    // 채팅방 입장
    @Override
    public Object enterChatRoom(ChatDto chatDto) {

        if(iChatRoomRepository.existsByActorIdOrReceiverId(chatDto.getSenderId(), chatDto.getSenderId())){

            String chatRoomId = chatDto.getChatRoomId();
            ChannelTopic topic = topics.get(chatRoomId);

            if (topic == null) {
                topic = new ChannelTopic(chatRoomId);
                redisMessageListener.addMessageListener(redisSubscriber, topic);
                topics.put(chatRoomId, topic);
            }
            return "채팅방에 입장했습니다.";
        }

        return "채팅방 참여자가 아닙니다.";
    }

    @Override
    public ChatRoom findRoomById(String roomId) {
        return iChatRoomRepository.findById(roomId).get();
    }

    @Override
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }


    public Chat findOneByChatRoomId(String roomId) {
        return iChatRepository.findOneByChatRoomId(roomId).get();
    }

    @Override
    public Set<String> getTopics() {
        return topics.keySet();
    }
}
