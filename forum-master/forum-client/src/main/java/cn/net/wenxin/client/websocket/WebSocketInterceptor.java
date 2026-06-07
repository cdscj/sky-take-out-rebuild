package cn.net.wenxin.client.websocket;

import cn.net.wenxin.common.core.domain.model.LoginUser;
import cn.net.wenxin.framework.web.service.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Component
public class WebSocketInterceptor implements HandshakeInterceptor {

    private static final Logger log = LoggerFactory.getLogger(WebSocketInterceptor.class);

    @Value("${token.header}")
    private String tokenHeader;

    @Autowired
    private TokenService tokenService;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                  WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpServletRequest httpServletRequest = servletRequest.getServletRequest();

            // Extract token from query parameter or Authorization header
            String token = httpServletRequest.getParameter("token");
            if (token == null || token.isEmpty()) {
                token = httpServletRequest.getHeader(tokenHeader);
                if (token != null && token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
            }

            if (token == null || token.isEmpty()) {
                log.warn("WebSocket 握手失败: 缺少认证令牌");
                response.setStatusCode(HttpStatus.UNAUTHORIZED);
                return false;
            }

            try {
                // 使用 TokenService 从 JWT + Redis 获取用户信息
                LoginUser loginUser = tokenService.getLoginUserByToken(token);
                if (loginUser == null) {
                    log.warn("WebSocket 握手失败: Token 无效或已过期");
                    response.setStatusCode(HttpStatus.UNAUTHORIZED);
                    return false;
                }
                // 从 AppUser 或 SysUser 中提取 userId
                Long userId = null;
                if (loginUser.getAppUser() != null) {
                    userId = loginUser.getAppUser().getId();
                } else if (loginUser.getUser() != null) {
                    userId = loginUser.getUser().getUserId();
                }
                if (userId != null) {
                    attributes.put("userId", userId);
                    log.debug("WebSocket 认证成功, userId={}", userId);
                    return true;
                }
                log.warn("WebSocket 握手失败: 无法获取 userId");
            } catch (Exception e) {
                log.warn("WebSocket JWT 验证失败: {}", e.getMessage());
            }
            response.setStatusCode(HttpStatus.UNAUTHORIZED);
            return false;
        }
        log.warn("WebSocket 握手失败: 不支持的请求类型");
        response.setStatusCode(HttpStatus.BAD_REQUEST);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                              WebSocketHandler wsHandler, Exception exception) {
        // 握手后的处理
    }
}
