package com.sparos.uniquone.msachatservice.chat.service;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.domain.ChatRoom;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import com.sparos.uniquone.msachatservice.chat.dto.chatRoomDto.ChatRoomCreateDto;
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
import java.util.stream.Collectors;

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

    // ?????? ????????? ??????
    @Override
    public List<ChatRoom> findAllRoom() {
        return iChatRoomRepository.findAll();
    }

    // ?????? ????????? ??????
    @Override
    public JSONObject findAllUserRoom(HttpServletRequest request) {

        Long userId = JwtProvider.getUserPkId(request);
        JSONObject jsonObject = new JSONObject();

        List<ChatRoom> chatRooms = iChatRoomRepository.findByActorIdAndIsActorOrReceiverIdAndIsReceiver(userId, true, userId, true);

        if (chatRooms.isEmpty()) {
            throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);
        }

        List<ChatRoomOutDto> chatRoomOutDtos = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            if (chatRoom.getReceiverId().equals(userId)) {
                chatRoom.setReceiverId(chatRoom.getActorId());
                chatRoom.setReceiver(chatRoom.getIsActor());
                chatRoom.setChatType(chatRoom.getChatType().equals(ChatRoomType.BUYER) ? ChatRoomType.SELLER : ChatRoomType.BUYER);
            }
            Optional<Chat> chat = iChatRepository.findOneByChatRoomId(chatRoom.getId());

            if (!chat.isPresent()){
                iChatRoomRepository.deleteById(chatRoom.getId());
                continue;
            } else {
                ChatRoomOutDto chatRoomOutDto = ChatUtils.entityToChatRoomOutDto(
                        chat.get(),
                        chatRoom,
                        iUserConnect.getUserInfo(chatRoom.getReceiverId()),
                        iPostConnect.getPostInfo(chatRoom.getPostId(), chatRoom.getReceiverId()));
                chatRoomOutDtos.add(chatRoomOutDto);
            }
        }

        jsonObject.put("data", chatRoomOutDtos.toArray());

        return jsonObject;
    }

    // ????????? ??????
    @Override
    public JSONObject createRoom(ChatRoomCreateDto chatRoomCreateDto, HttpServletRequest request) {

        JSONObject jsonObject = new JSONObject();
        Long userId = JwtProvider.getUserPkId(request);
        Long receiverId = iPostConnect.getUserIdByCorn(chatRoomCreateDto.getPostId());

        if (!userId.equals(receiverId)) {
            Optional<ChatRoom> existChatRoom =
                    iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId
                            (chatRoomCreateDto.getPostId(), true, true, userId, receiverId,
                                    chatRoomCreateDto.getPostId(), true, true, receiverId, userId);

            Boolean existPost = false;

            if (existChatRoom.isPresent()) {

                jsonObject.put("data", existChatRoom.get());

            } else {

                if (chatRoomCreateDto.getChatType().equals(ChatRoomType.BUYER)) {
                    existPost = iPostConnect.getExistPost(chatRoomCreateDto.getPostId(), receiverId);

                } else if (chatRoomCreateDto.getChatType().equals(ChatRoomType.SELLER)) {
                    existPost = iPostConnect.getExistPost(chatRoomCreateDto.getPostId(), userId);
                }

                if (!existPost.equals(true)) {
                    throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);
                }
                ChatRoom chatRoom = iChatRoomRepository.save(
                        ChatRoom.builder()
                                .chatType(chatRoomCreateDto.getChatType())
                                .actorId(userId)
                                .receiverId(receiverId)
                                .postId(chatRoomCreateDto.getPostId())
                                .isActor(true)
                                .isReceiver(true)
//                                .regDate(chatRoomDto.getRegDate())
                                .build());
                jsonObject.put("data", chatRoom);
            }
        } else {
            jsonObject.put("data", "");
        }

        return jsonObject;
    }

    // ????????? ?????????
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

    // ????????? ??????
    @Override
    public JSONObject deleteRoom(String roomId) {

        JSONObject jsonObject = new JSONObject();
        iChatRoomRepository.findById(roomId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        iChatRoomRepository.deleteById(roomId);

        jsonObject.put("data", "???????????? ??????????????????.");
        return jsonObject;
    }

    // ?????? ?????? ??????
    @Override
    public JSONObject findAllChat(String roomId, HttpServletRequest request) {
        JSONObject jsonObject = new JSONObject();
        Long userId = JwtProvider.getUserPkId(request);

        ChatRoom chatRoom = iChatRoomRepository.findByIdAndActorIdOrIdAndReceiverId(roomId, userId, roomId, userId)
                .orElseThrow(() -> new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED));

        List<Chat> chats = iChatRepository.findByChatRoomId(roomId);

        if (chats.isEmpty()) {
            chats = null;
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

    // ?????? ??????
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

/*        iPostConnect.chatPush(
                ChatPushDto.builder()
                        .receiverId(chatRoom.getReceiverId())
                        .postId(chatRoom.getPostId())
                        .chat(chat)
                        .build());*/

        return chat;
    }

    // ????????? ??????
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

    // offer ?????? ??? ??? ????????? ?????? -> chatRoomId ??????
    @Override
    public void offerChat(ChatRoomDto chatRoomDto, String token) {

        Long userId = JwtProvider.getUserPkId(token);
        Long receiverId = chatRoomDto.getReceiverId();
        Optional<ChatRoom> existChatRoom =
                iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId
                        (chatRoomDto.getPostId(), true, true, userId, receiverId,
                                chatRoomDto.getPostId(), true, true, receiverId, userId);

        Boolean existPost = false;
        ChatRoom chatRoom = null;

        if (existChatRoom.isPresent()) {
            chatRoom = existChatRoom.get();
        } else {

            if (chatRoomDto.getChatType().equals(ChatRoomType.BUYER)) {
                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), receiverId);
            } else if (chatRoomDto.getChatType().equals(ChatRoomType.SELLER)) {
                existPost = iPostConnect.getExistPost(chatRoomDto.getPostId(), userId);
            }

            if (!existPost.equals(true))
                throw new UniquOneServiceException(ExceptionCode.NO_SUCH_ELEMENT_EXCEPTION, HttpStatus.ACCEPTED);

            chatRoom = iChatRoomRepository.save(
                    ChatRoom.builder()
                            .chatType(chatRoomDto.getChatType())
                            .actorId(userId)
                            .receiverId(receiverId)
                            .postId(chatRoomDto.getPostId())
                            .isActor(true)
                            .isReceiver(true)
//                            .regDate(chatRoomDto.getRegDate())
                            .build());
        }

        iChatRepository.save(
                Chat.builder()
                        .senderId(userId)
                        .chatRoomId(chatRoom.getId())
                        .message("????????? ?????????????????????. ???????????? : " + chatRoomDto.getPostPrice() + "->" + chatRoomDto.getOfferPrice())
                        .type(ChatType.NOTICE)
                        .build());

    }

    // ?????? ?????? chatRoomId
    @Override
    public String offerChat(Long postId, Long userId, Long receiverId) {

        String chatRoomId = null;
        Optional<ChatRoom> chatRoom = iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId(
                postId, true, true, userId, receiverId,
                postId, true, true, receiverId, userId);

        // todo ????????? ????????? ?????? ?????? ????????????
        if (chatRoom.isPresent()) {
            chatRoomId = chatRoom.get().getId();
        } else {

            chatRoom = iChatRoomRepository.findOneByPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverIdOrPostIdAndIsActorAndIsReceiverAndActorIdAndReceiverId(
                    postId, false, true, userId, receiverId,
                    postId, true, false, receiverId, userId);

            if (chatRoom.isPresent()) {
                // ?????? ????????? ?????? ?????? ??????
                chatRoom.get().setActor(true);
                chatRoom.get().setReceiver(true);
                chatRoomId = chatRoom.get().getId();
            } else {
                // ??? ??? ????????? ??????, ????????? ????????? ?????? ????????? ????????? ?????????
                ChatRoom chatRoomCreated = iChatRoomRepository.save(
                        ChatRoom.builder()
                                .chatType(ChatRoomType.SELLER)
                                .actorId(userId)
                                .receiverId(receiverId)
                                .postId(postId)
                                .isActor(true)
                                .isReceiver(true)
                                .build());
                chatRoomId = chatRoomCreated.getId();
            }

        }

        return chatRoomId;
    }
}
