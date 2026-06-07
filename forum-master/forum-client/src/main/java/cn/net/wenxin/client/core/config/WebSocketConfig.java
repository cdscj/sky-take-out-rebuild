package cn.net.wenxin.client.core.config;

import cn.net.wenxin.client.websocket.MessageWebSocketHandler;
import cn.net.wenxin.client.websocket.WebSocketInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;


@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Value("${spring.websocket.allowed-origins:*}")
    private String allowedOrigins;

    @Autowired
    private MessageWebSocketHandler messageWebSocketHandler;

    @Autowired
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        String[] origins = allowedOrigins.split(",");
        registry.addHandler(messageWebSocketHandler, "/ws/messages")
                .addInterceptors(webSocketInterceptor)
                .setAllowedOrigins(origins);
    }
}
