package cn.net.wenxin.mapper;

;
import cn.net.wenxin.client.entity.Friendship;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface FriendshipMapper {
    Friendship selectFriendship(Long userId, Long friendId);
}