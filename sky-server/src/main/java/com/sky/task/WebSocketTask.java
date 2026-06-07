package com.sky.task;

import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * WebSocket 调试任务 — 仅在显式启用时激活，生产环境请勿启用。
 * 设置 spring.websocket.debug-enabled=true 以开启调试广播。
 */
@Component
@ConditionalOnProperty(name = "spring.websocket.debug-enabled", havingValue = "true", matchIfMissing = false)
@Slf4j
public class WebSocketTask {

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 通过WebSocket每分钟向客户端发送调试消息
     */
    @Scheduled(cron = "0 * * * * ?")
    public void sendMessageToClient() {
        log.debug("发送WebSocket调试消息");
        webSocketServer.sendToAllClient("这是来自服务端的消息：" +
                DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()));
    }
}
