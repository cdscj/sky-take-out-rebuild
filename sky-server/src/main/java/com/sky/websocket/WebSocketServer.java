package com.sky.websocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket服务
 */
@Component
@ServerEndpoint("/ws/{sid}")
@Slf4j
public class WebSocketServer {

    // 存放会话对象（线程安全）
    private static final Map<String, Session> sessionMap = new ConcurrentHashMap<>();

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("sid") String sid) {
        log.info("客户端：{} 建立连接", sid);
        sessionMap.put(sid, session);
    }

    /**
     * 收到客户端消息后调用的方法
     */
    @OnMessage
    public void onMessage(String message, @PathParam("sid") String sid) {
        log.debug("收到来自客户端：{} 的信息：{}", sid, message);
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(@PathParam("sid") String sid) {
        log.info("连接断开：{}", sid);
        sessionMap.remove(sid);
    }

    /**
     * 连接发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebSocket 错误，会话ID：{}", session.getId(), error);
        // 按 key 精确移除，避免 O(n) 遍历 values
        sessionMap.entrySet().removeIf(entry -> entry.getValue().equals(session));
    }

    /**
     * 群发消息
     */
    public void sendToAllClient(String message) {
        Collection<Session> sessions = sessionMap.values();
        for (Session session : sessions) {
            if (session.isOpen()) {
                try {
                    session.getBasicRemote().sendText(message);
                } catch (Exception e) {
                    log.error("群发消息失败", e);
                }
            }
        }
    }

    /**
     * 发送消息给指定骑手
     */
    public void sendRiderInfo(String message, Long courierId) {
        Session session = sessionMap.get("rider_" + courierId);
        if (session != null && session.isOpen()) {
            try {
                session.getBasicRemote().sendText(message);
            } catch (IOException e) {
                log.error("发送消息给骑手 {} 失败", courierId, e);
            }
        }
    }

}
