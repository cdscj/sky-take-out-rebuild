package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.UserPoints;

/**
 * 用户积分Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface IUserPointsService 
{
    /**
     * 查询用户积分
     * 
     * @param id 用户积分主键
     * @return 用户积分
     */
    public UserPoints selectUserPointsById(Long id);

    /**
     * 查询用户积分列表
     * 
     * @param userPoints 用户积分
     * @return 用户积分集合
     */
    public List<UserPoints> selectUserPointsList(UserPoints userPoints);

    /**
     * 新增用户积分
     * 
     * @param userPoints 用户积分
     * @return 结果
     */
    public int insertUserPoints(UserPoints userPoints);

    /**
     * 修改用户积分
     * 
     * @param userPoints 用户积分
     * @return 结果
     */
    public int updateUserPoints(UserPoints userPoints);

    /**
     * 批量删除用户积分
     * 
     * @param ids 需要删除的用户积分主键集合
     * @return 结果
     */
    public int deleteUserPointsByIds(Long[] ids);

    /**
     * 删除用户积分信息
     * 
     * @param id 用户积分主键
     * @return 结果
     */
    public int deleteUserPointsById(Long id);

    /**
     *
     * @param userId
     * @param type
     * @param rid
     */
    public void addUserPoints(String userId, String type,Long rid);
}
