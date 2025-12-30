package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.UserFollow;
import org.apache.ibatis.annotations.Param;

/**
 * 用户关注主题Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-28
 */
public interface UserFollowMapper 
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
     * 删除用户关注主题
     * 
     * @param topicId 用户关注主题主键
     * @return 结果
     */
    public int deleteUserFollowByTopicId(Long topicId);

    /**
     * 批量删除用户关注主题
     * 
     * @param topicIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserFollowByTopicIds(Long[] topicIds);

    /**
     * 是否关注
     * @param topicId
     * @param userName
     * @return
     */
    public int selectFollowByTopicIdAndUser(@Param("topicId") Long topicId, @Param("userName")String userName);

    /**
     * 取消关注
     * @param follow
     * @return
     */
    public int deleteFollowByTopicIdAndUser(UserFollow follow);
}
