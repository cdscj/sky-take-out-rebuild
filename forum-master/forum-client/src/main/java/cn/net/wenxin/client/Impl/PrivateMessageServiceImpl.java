package cn.net.wenxin.client.Impl;



import cn.net.wenxin.client.entity.PrivateMessage;
import cn.net.wenxin.client.service.FriendshipService;
import cn.net.wenxin.client.service.PrivateMessageService;
import cn.net.wenxin.client.websocket.MessageWebSocketHandler;
import cn.net.wenxin.mapper.PrivateMessageMapper;
import cn.net.wenxin.service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

@Service
public class PrivateMessageServiceImpl implements PrivateMessageService {

    @Autowired
    private PrivateMessageMapper privateMessageMapper;

    @Autowired
    private IUserService userService;

    @Autowired
    private FriendshipService friendshipService;

    @Autowired
    private MessageWebSocketHandler messageWebSocketHandler;

    @Override
    public void sendMessage(Long senderId, Long receiverId, String content) {
        // 验证发送者是否存在且状态正常
        if (!userService.isUserValid(senderId)) {
            throw new IllegalArgumentException("发送者不存在或状态异常");
        }

        // 验证接收者是否存在且状态正常
        if (!userService.isUserValid(receiverId)) {
            throw new IllegalArgumentException("接收者不存在或状态异常");
        }

        // 验证发送者和接收者是否为好友关系
        if (!friendshipService.isFriend(senderId, receiverId)) {
            throw new IllegalArgumentException("您不是对方的好友，无法发送私信");
        }

        // 创建私信对象
        PrivateMessage message = new PrivateMessage();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setContent(content);
        message.setSendTime(new Date());
        message.setIsRead(false);

        // 存储私信到数据库
        privateMessageMapper.insert(message);

        // 如果接收者在线，通过WebSocket推送消息
        if (messageWebSocketHandler.isUserOnline(receiverId)) {
            messageWebSocketHandler.sendMessageToUser(receiverId, message);
        }
    }

    @Override
    public List<PrivateMessage> getUnreadMessages(Long userId) {
        return privateMessageMapper.selectUnreadByReceiverId(userId);
    }

    @Override
    public List<PrivateMessage> getMessagesBetweenUsers(Long userId1, Long userId2) {
        return privateMessageMapper.selectBySenderAndReceiver(userId1, userId2);
    }

    @Override
    public void markAsRead(Long messageId) {
        privateMessageMapper.updateReadStatus(messageId, true);
    }

    @Override
    public void markBatchAsRead(List<Long> messageIds) {
        if (messageIds != null && !messageIds.isEmpty()) {
            privateMessageMapper.updateBatchReadStatus(messageIds, true);
        }
    }
}