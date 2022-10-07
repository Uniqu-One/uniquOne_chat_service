package com.sparos.uniquone.msachatservice.chat.service.redis;

import com.sparos.uniquone.msachatservice.chat.domain.Chat;
import com.sparos.uniquone.msachatservice.chat.dto.chatDto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class RedisPublisher {
    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, Chat chat) {
        redisTemplate.convertAndSend(topic.getTopic(), chat);
    }

}