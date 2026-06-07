package cn.net.wenxin.client.entity;

import lombok.Data;
import java.util.Date;

@Data
public class PrivateMessage {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private String content;
    private Date sendTime;
    private Boolean isRead;
    private Date createdAt;
}