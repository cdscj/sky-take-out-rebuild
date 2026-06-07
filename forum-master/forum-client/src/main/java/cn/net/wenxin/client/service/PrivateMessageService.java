package cn.net.wenxin.client.service;


import cn.net.wenxin.client.entity.PrivateMessage;

import java.util.List;

public interface PrivateMessageService {
    void sendMessage(Long senderId, Long receiverId, String content);
    List<PrivateMessage> getUnreadMessages(Long userId);
    List<PrivateMessage> getMessagesBetweenUsers(Long userId1, Long userId2);
    void markAsRead(Long messageId);
    void markBatchAsRead(List<Long> messageIds);
}