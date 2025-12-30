package cn.net.wenxin.service.mapper;

import java.util.List;
import java.util.Map;

import cn.net.wenxin.service.domain.UserPoints;
import org.apache.ibatis.annotations.Param;

/**
 * 用户积分Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface UserPointsMapper 
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
     * 删除用户积分
     * 
     * @param id 用户积分主键
     * @return 结果
     */
    public int deleteUserPointsById(Long id);

    /**
     * 批量删除用户积分
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteUserPointsByIds(Long[] ids);

    UserPoints selectUserPointsByUserId(@Param("userId") String userId);

    Map<String, Object> getUserPointLevel(@Param("userId") String userId);
}
