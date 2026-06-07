package cn.net.wenxin.client.Impl;




import cn.net.wenxin.client.entity.Friendship;
import cn.net.wenxin.client.service.FriendshipService;
import cn.net.wenxin.mapper.FriendshipMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FriendshipServiceImpl implements FriendshipService {

    @Autowired
    private FriendshipMapper friendshipMapper;

    @Override
    public boolean isFriend(Long userId, Long friendId) {
        Friendship friendship = friendshipMapper.selectFriendship(userId, friendId);
        return friendship != null && friendship.getStatus() == 1;
    }
}