package com.sparos.uniquone.msachatservice.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat/ws-stomp")
                .setAllowedOriginPatterns("http://13.125.66.71:8000","http://localhost:3000", "http://10.10.10.143:3000","http://13.125.66.71:80",
                        "https://www.uniquone.shop","https://uniquone.shop", "http://www.uniquone.shop", "http://uniquone.shop", "http://10.10.10.126:3000","http://13.125.66.71:3000")
                .withSockJS().setSupressCors(true);
    }
}
