package com.sparos.uniquone.msachatservice.chat.service;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomOutDto;
import com.sparos.uniquone.msachatservice.outband.post.dto.ChatPushDto;
import com.sparos.uniquone.msachatservice.utils.enums.ChatRoomType;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRepository;
import com.sparos.uniquone.msachatservice.chat.repository.IChatRoomRepository;
import com.sparos.uniquone.msachatservice.chat.service.redis.RedisSubscriber;
import com.sparos.uniquone.msachatservice.outband.post.service.IPostConnect;
import com.sparos.uniquone.msachatservice.outband.user.service.IUserConnect;
import com.sparos.uniquone.msachatservice.utils.ChatUtils;
import com.sparos.uniquone.msachatservice.utils.enums.ChatType;
import com.sparos.uniquone.msachatservice.utils.jwt.JwtProvider;
import com.sparos.uniquone.msachatservice.utils.response.ExceptionCode;
import com.sparos.uniquone.msachatservice.utils.response.UniquOneServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
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

    private final RedisMessageListenerContainer redisMessageListener;
    private final RedisSubscriber redisSubscriber;

    private Map<String, ChannelTopic> topics;

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
    public JSONObject findAllUserRoom(HttpServletRequest request) {

        Long userId = JwtProvider.getUserPkId(request);
        JSONObject jsonObject = new JSONObject();
        List<ChatRoomOutDto> chatRoomOutDtos = new ArrayList<>();
        List<ChatRoom> chatRooms = iChatRoomRepository.findByActorIdAndIsActorOrReceiverIdAndIsReceiver(userId, true, userId, true);

        if (chatRooms.isEmpty()) {
            throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);
        }

        chatRooms.forEach(chatRoom -> {
            if (chatRoom.getReceiverId().equals(userId)) {
                chatRoom.setReceiverId(chatRoom.getActorId());
                chatRoom.setReceiver(chatRoom.getIsActor());
                chatRoom.setChatType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
            }

            Optional<Chat> chat = iChatRepository.findOneByChatRoomId(chatRoom.getId());
//                    .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.NO_CONTENT));

            if (chat.isPresent()) {
                chatRoomOutDtos.add(ChatUtils.entityToChatRoomOutDto(
                        chat.get(),
                        chatRoom,
                        iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                        iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()))
                );
            }else { // todo 지울 코드
                chatRoomOutDtos.add(ChatUtils.entityToChatRoomOutDto(
                        Chat.builder().message("최근메시지").regDate(null).build(),
                        chatRoom,
                        iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                        iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()))
                );
            }
        });
        jsonObject.put("data", chatRoomOutDtos.toArray());

        return jsonObject;
    }

    // 채팅방 생성
    @Override
    public JSONObject createRoom(ChatRoomDto chatRoomDto, HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        Long userId = JwtProvider.getUserPkId(request);
        Optional<ChatRoom> existChatRoom =
                iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId
                        (chatRoomDto.getPostId(), true, true, userId, chatRoomDto.getReceiverId(),
                                chatRoomDto.getPostId(), true, true, chatRoomDto.getReceiverId(), userId);

        Boolean existPost = false;

        if (existChatRoom.isPresent()) {

            jsonObject.put("data", existChatRoom.get());

        } else {

            if (chatRoomDto.getChatType().equals(ChatRoomType.BUYER)) {

                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), chatRoomDto.getReceiverId());

            } else if (chatRoomDto.getChatType().equals(ChatRoomType.SELLER)) {

                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), userId);
            }

            if (!existPost.equals(true)) {
                throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);
            }
            ChatRoom chatRoom = iChatRoomRepository.save(
                    ChatRoom.builder()
                            .chatType(chatRoomDto.getChatType())
                            .actorId(userId)
                            .receiverId(chatRoomDto.getReceiverId())
                            .postId(chatRoomDto.getPostId())
                            .isActor(true)
                            .isReceiver(true)
                            .regDate(chatRoomDto.getRegDate())
                            .build());
            jsonObject.put("data", chatRoom);
        }

        return jsonObject;
    }

    // 채팅방 나가기
    @Override
    public JSONObject exitRoom(String chatRoomId, HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        Long userId = JwtProvider.getUserPkId(request);
        ChatRoom chatRoom = iChatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        if (chatRoom.getReceiverId().equals(userId)) {
            chatRoom.setReceiver(false);
        } else {
            chatRoom.setActor(false);
        }
        chatRoom = iChatRoomRepository.save(chatRoom);

        jsonObject.put("data", chatRoom);
        return jsonObject;
    }

    // 채팅방 삭제
    @Override
    public JSONObject deleteRoom(String roomId) {

        JSONObject jsonObject = new JSONObject();
        ChatRoom chatRoom = iChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        iChatRoomRepository.delete(chatRoom);

        jsonObject.put("data", "채팅방을 삭제했습니다.");
        return jsonObject;
    }

    // 채팅 내용 조회
    @Override
    public JSONObject findAllChat(String roomId, HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        Long userId = JwtProvider.getUserPkId(request);
        ChatRoom chatRoomOptional = iChatRoomRepository.findByIdAndActorIdOrIdAndReceiverId(roomId, userId, roomId, userId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        List<Chat> chats = iChatRepository.findByChatRoomId(roomId);
        ChatRoom chatRoom = chatRoomOptional;

        if (chats.isEmpty()) {
            throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);
        }
        if (chatRoom.getReceiverId().equals(userId)) {
            chatRoom.setReceiverId(chatRoom.getActorId());
            chatRoom.setReceiver(chatRoom.getIsActor());
            chatRoom.setChatType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
        }

        jsonObject.put("data", ChatUtils.entityToChatOutDto(
                chatRoom,
                iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()),
                chats));

        return jsonObject;
    }

    // 채팅 전송
    @Override
    public Chat sendChat(ChatDto chatDto, String token) {
        Long userId = JwtProvider.getUserPkId(token);
        ChatRoom chatRoom = iChatRoomRepository.findByIdAndActorIdOrIdAndReceiverId(chatDto.getChatRoomId(), userId, chatDto.getChatRoomId(), userId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        Chat chat = iChatRepository.save(
                Chat.builder()
                        .senderId(userId)
                        .chatRoomId(chatRoom.getId())
                        .message(chatDto.getMessage())
                        .regDate(chatDto.getRegDate())
                        .type(chatDto.getType())
                        .build());

        iPostConnect.chatPush(
                ChatPushDto.builder()
                        .receiverId(chatRoom.getReceiverId())
                        .postId(chatRoom.getPostId())
                        .chat(chat)
                .build());

        return chat;
    }

    // 채팅방 입장
    @Override
    public void enterChatRoom(ChatDto chatDto, String token) {

        Long userId = JwtProvider.getUserPkId(token);

        if (!iChatRoomRepository.existsByActorIdOrReceiverId(userId, userId)) {
            throw new UniquOneServiceException(ExceptionCode.DON_T_HAVE_ACCESS, HttpStatus.ACCEPTED);
        }

        String chatRoomId = chatDto.getChatRoomId();
        ChannelTopic topic = topics.get(chatRoomId);

        if (topic == null) {
            topic = new ChannelTopic(chatRoomId);
            redisMessageListener.addMessageListener(redisSubscriber, topic);
            topics.put(chatRoomId, topic);
        }
    }

    @Override
    public ChatRoom findRoomById(String roomId) {
        return iChatRoomRepository.findById(roomId).get();
    }

    @Override
    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    @Override
    public Set<String> getTopics() {
        return topics.keySet();
    }

    // offer 수락 일 때 채팅방 생성 -> chatRoomId 리턴
    @Override
    public void offerChat(ChatRoomDto chatRoomDto, String token) {

        Long userId = JwtProvider.getUserPkId(token);
        Optional<ChatRoom> existChatRoom =
                iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId
                        (chatRoomDto.getPostId(), true, true, userId, chatRoomDto.getReceiverId(),
                                chatRoomDto.getPostId(), true, true, chatRoomDto.getReceiverId(), userId);

        Boolean existPost = false;
        ChatRoom chatRoom = null;

        if (existChatRoom.isPresent()) {
            chatRoom = existChatRoom.get();
        } else {

            if (chatRoomDto.getChatType().equals(ChatRoomType.BUYER)) {
                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), chatRoomDto.getReceiverId());
            } else if (chatRoomDto.getChatType().equals(ChatRoomType.SELLER)) {
                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), userId);
            }

            if (!existPost.equals(true))
                throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);

            chatRoom = iChatRoomRepository.save(
                    ChatRoom.builder()
                            .chatType(chatRoomDto.getChatType())
                            .actorId(userId)
                            .receiverId(chatRoomDto.getReceiverId())
                            .postId(chatRoomDto.getPostId())
                            .isActor(true)
                            .isReceiver(true)
                            .regDate(chatRoomDto.getRegDate())
                            .build());
        }

        iChatRepository.save(
                Chat.builder()
                        .senderId(userId)
                        .chatRoomId(chatRoom.getId())
                        .message("오퍼가 수락되었습니다. 오퍼가격 : N원")
                        .type(ChatType.NOTICE)
                        .build());

    }

    @Override
    public String offerChat(Long postId, Long userId, Long receiverId) {

        // todo 채팅방 나가기 했을 경우 처리하기
        ChatRoom chatRoom = iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId(
                        postId, true, true, userId, receiverId,
                        postId, true, true, receiverId, userId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        return chatRoom.getId();
    }
}
