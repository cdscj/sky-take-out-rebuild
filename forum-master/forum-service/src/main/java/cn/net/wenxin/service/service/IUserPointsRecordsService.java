package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.UserPointsRecords;

/**
 * 用户积分详情Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface IUserPointsRecordsService 
{
    /**
     * 查询用户积分详情
     * 
     * @param id 用户积分详情主键
     * @return 用户积分详情
     */
    public UserPointsRecords selectUserPointsRecordsById(Long id);

    /**
     * 查询用户积分详情列表
     * 
     * @param userPointsRecords 用户积分详情
     * @return 用户积分详情集合
     */
    public List<UserPointsRecords> selectUserPointsRecordsList(UserPointsRecords userPointsRecords);

    /**
     * 新增用户积分详情
     * 
     * @param userPointsRecords 用户积分详情
     * @return 结果
     */
    public int insertUserPointsRecords(UserPointsRecords userPointsRecords);

    /**
     * 修改用户积分详情
     * 
     * @param userPointsRecords 用户积分详情
     * @return 结果
     */
    public int updateUserPointsRecords(UserPointsRecords userPointsRecords);

    /**
     * 批量删除用户积分详情
     * 
     * @param ids 需要删除的用户积分详情主键集合
     * @return 结果
     */
    public int deleteUserPointsRecordsByIds(Long[] ids);

    /**
     * 删除用户积分详情信息
     * 
     * @param id 用户积分详情主键
     * @return 结果
     */
    public int deleteUserPointsRecordsById(Long id);
}
