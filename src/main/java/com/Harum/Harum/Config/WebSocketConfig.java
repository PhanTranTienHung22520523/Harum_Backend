package com.Harum.Harum.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Broker gửi tin nhắn tới client
        config.enableSimpleBroker("/queue", "/topic");
        // Prefix các message client gửi lên server
        config.setApplicationDestinationPrefixes("/app");
        // Prefix gửi đến user riêng lẻ (private queue)
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint để client connect tới WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // cho phép mọi domain (cần điều chỉnh khi production)
                .withSockJS();                  // fallback SockJS nếu không hỗ trợ WebSocket
    }
}
