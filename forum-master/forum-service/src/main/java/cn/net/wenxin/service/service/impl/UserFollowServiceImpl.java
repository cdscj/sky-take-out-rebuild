package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.service.service.IUserPointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.UserFollowMapper;
import cn.net.wenxin.service.domain.UserFollow;
import cn.net.wenxin.service.service.IUserFollowService;

/**
 * 用户关注主题Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-28
 */
@Service
public class UserFollowServiceImpl implements IUserFollowService 
{
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private IUserPointsService userPointsService;

    /**
     * 查询用户关注主题
     * 
     * @param topicId 用户关注主题主键
     * @return 用户关注主题
     */
    @Override
    public UserFollow selectUserFollowByTopicId(Long topicId)
    {
        return userFollowMapper.selectUserFollowByTopicId(topicId);
    }

    /**
     * 查询用户关注主题列表
     * 
     * @param userFollow 用户关注主题
     * @return 用户关注主题
     */
    @Override
    public List<UserFollow> selectUserFollowList(UserFollow userFollow)
    {
        return userFollowMapper.selectUserFollowList(userFollow);
    }

    /**
     * 新增用户关注主题
     * 
     * @param userFollow 用户关注主题
     * @return 结果
     */
    @Override
    public int insertUserFollow(UserFollow userFollow)
    {
        int r = userFollowMapper.insertUserFollow(userFollow);
        userPointsService.addUserPoints(userFollow.getUserId(),"5",userFollow.getTopicId());
        return r;
    }

    /**
     * 修改用户关注主题
     * 
     * @param userFollow 用户关注主题
     * @return 结果
     */
    @Override
    public int updateUserFollow(UserFollow userFollow)
    {
        return userFollowMapper.updateUserFollow(userFollow);
    }

    /**
     * 批量删除用户关注主题
     * 
     * @param topicIds 需要删除的用户关注主题主键
     * @return 结果
     */
    @Override
    public int deleteUserFollowByTopicIds(Long[] topicIds)
    {
        return userFollowMapper.deleteUserFollowByTopicIds(topicIds);
    }

    /**
     * 删除用户关注主题信息
     * 
     * @param topicId 用户关注主题主键
     * @return 结果
     */
    @Override
    public int deleteUserFollowByTopicId(Long topicId)
    {
        return userFollowMapper.deleteUserFollowByTopicId(topicId);
    }

    @Override
    public int unfollow(UserFollow follow) {
        return userFollowMapper.deleteFollowByTopicIdAndUser(follow);
    }
}
