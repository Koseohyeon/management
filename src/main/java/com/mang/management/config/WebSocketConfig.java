package com.mang.management.config;

import com.mang.management.jwt.JwtHandshakeInterceptor;
import com.mang.management.jwt.JwtUtil;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;

@Configuration
@EnableWebSocketMessageBroker
@AllArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final JwtUtil jwtUtil;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/topic"); // 구독 엔드포인트 (클라이언트가 메시지 받는 곳)
        config.setApplicationDestinationPrefixes("/app"); //메시지를 처리할 엔드포인트(prefix)
    }
    @Override
    public void configureWebSocketTransport(WebSocketTransportRegistration registry) {
        registry.setMessageSizeLimit(8192);  // 메시지 크기 설정
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // 표준 WebSocket 및 SockJS 지원
                .setAllowedOrigins("http://localhost:3000")
                .addInterceptors(new JwtHandshakeInterceptor(jwtUtil));
                //.withSockJS(); // SockJS 활성화
    }
}
