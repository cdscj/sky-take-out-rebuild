package cn.net.wenxin.client.websocket;

import cn.net.wenxin.client.entity.PrivateMessage;
import cn.net.wenxin.service.service.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class MessageWebSocketHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageWebSocketHandler.class);

    // 维护用户ID和WebSocket会话的映射关系
    private static final Map<Long, WebSocketSession> USER_SESSIONS = new ConcurrentHashMap<>();

    @Autowired
    private IUserService userService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            USER_SESSIONS.put(userId, session);
            userService.updateOnlineStatus(userId, 1);
            log.info("用户 {} 已连接WebSocket", userId);
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Long userId = (Long) session.getAttributes().get("userId");
        String payload = message.getPayload();
        log.debug("收到来自用户 {} 的消息: {}", userId, payload);
        // NOTE: 消息解析和转发逻辑待实现
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) {
            USER_SESSIONS.remove(userId);
            userService.updateOnlineStatus(userId, 0);
            log.info("用户 {} 已断开WebSocket连接, status={}", userId, status);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        Long userId = (Long) session.getAttributes().get("userId");
        log.error("WebSocket 传输错误, userId={}, error={}", userId, exception.getMessage());
        if (userId != null) {
            USER_SESSIONS.remove(userId);
            userService.updateOnlineStatus(userId, 0);
        }
        try {
            session.close(CloseStatus.SERVER_ERROR);
        } catch (IOException e) {
            log.error("关闭异常会话失败", e);
        }
    }

    /**
     * 发送消息给指定用户
     */
    public void sendMessageToUser(Long userId, PrivateMessage message) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        if (session != null && session.isOpen()) {
            try {
                String messageJson = objectMapper.writeValueAsString(message);
                session.sendMessage(new TextMessage(messageJson));
            } catch (IOException e) {
                log.error("发送消息到用户 {} 失败", userId, e);
            }
        }
    }

    /**
     * 获取用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        WebSocketSession session = USER_SESSIONS.get(userId);
        return session != null && session.isOpen();
    }
}
