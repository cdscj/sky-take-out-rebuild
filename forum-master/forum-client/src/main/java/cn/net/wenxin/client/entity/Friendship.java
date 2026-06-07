package cn.net.wenxin.client.entity;

import lombok.Data;
import java.util.Date;

@Data
public class Friendship {
    private Long id;
    private Long userId;
    private Long friendId;
    private Integer status;
    private Date createdAt;
}