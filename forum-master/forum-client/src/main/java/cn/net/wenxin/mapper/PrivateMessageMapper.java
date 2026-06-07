package cn.net.wenxin.mapper;

;
import cn.net.wenxin.client.entity.PrivateMessage;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface PrivateMessageMapper {
    int insert(PrivateMessage message);
    List<PrivateMessage> selectUnreadByReceiverId(Long receiverId);
    List<PrivateMessage> selectBySenderAndReceiver(Long senderId, Long receiverId);
    int updateReadStatus(Long id, Boolean isRead);
    int updateBatchReadStatus(List<Long> ids, Boolean isRead);
}