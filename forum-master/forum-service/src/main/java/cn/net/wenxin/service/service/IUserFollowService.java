package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.UserFollow;

/**
 * 用户关注主题Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-28
 */
public interface IUserFollowService 
{
    /**
     * 查询用户关注主题
     * 
     * @param topicId 用户关注主题主键
     * @return 用户关注主题
     */
    public UserFollow selectUserFollowByTopicId(Long topicId);

    /**
     * 查询用户关注主题列表
     * 
     * @param userFollow 用户关注主题
     * @return 用户关注主题集合
     */
    public List<UserFollow> selectUserFollowList(UserFollow userFollow);

    /**
     * 新增用户关注主题
     * 
     * @param userFollow 用户关注主题
     * @return 结果
     */
    public int insertUserFollow(UserFollow userFollow);

    /**
     * 修改用户关注主题
     * 
     * @param userFollow 用户关注主题
     * @return 结果
     */
    public int updateUserFollow(UserFollow userFollow);

    /**
     * 批量删除用户关注主题
     * 
     * @param topicIds 需要删除的用户关注主题主键集合
     * @return 结果
     */
    public int deleteUserFollowByTopicIds(Long[] topicIds);

    /**
     * 删除用户关注主题信息
     * 
     * @param topicId 用户关注主题主键
     * @return 结果
     */
    public int deleteUserFollowByTopicId(Long topicId);

    /**
     * 取消关注
     * @param follow
     * @return
     */
    public int unfollow(UserFollow follow);
}
