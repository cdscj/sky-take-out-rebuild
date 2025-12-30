package cn.net.wenxin.service.service.impl;

import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.UserPointsRecordsMapper;
import cn.net.wenxin.service.domain.UserPointsRecords;
import cn.net.wenxin.service.service.IUserPointsRecordsService;

/**
 * 用户积分详情Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class UserPointsRecordsServiceImpl implements IUserPointsRecordsService 
{
    @Autowired
    private UserPointsRecordsMapper userPointsRecordsMapper;

    /**
     * 查询用户积分详情
     * 
     * @param id 用户积分详情主键
     * @return 用户积分详情
     */
    @Override
    public UserPointsRecords selectUserPointsRecordsById(Long id)
    {
        return userPointsRecordsMapper.selectUserPointsRecordsById(id);
    }

    /**
     * 查询用户积分详情列表
     * 
     * @param userPointsRecords 用户积分详情
     * @return 用户积分详情
     */
    @Override
    public List<UserPointsRecords> selectUserPointsRecordsList(UserPointsRecords userPointsRecords)
    {
        return userPointsRecordsMapper.selectUserPointsRecordsList(userPointsRecords);
    }

    /**
     * 新增用户积分详情
     * 
     * @param userPointsRecords 用户积分详情
     * @return 结果
     */
    @Override
    public int insertUserPointsRecords(UserPointsRecords userPointsRecords)
    {
        userPointsRecords.setCreateTime(DateUtils.getNowDate());
        return userPointsRecordsMapper.insertUserPointsRecords(userPointsRecords);
    }

    /**
     * 修改用户积分详情
     * 
     * @param userPointsRecords 用户积分详情
     * @return 结果
     */
    @Override
    public int updateUserPointsRecords(UserPointsRecords userPointsRecords)
    {
        userPointsRecords.setUpdateTime(DateUtils.getNowDate());
        return userPointsRecordsMapper.updateUserPointsRecords(userPointsRecords);
    }

    /**
     * 批量删除用户积分详情
     * 
     * @param ids 需要删除的用户积分详情主键
     * @return 结果
     */
    @Override
    public int deleteUserPointsRecordsByIds(Long[] ids)
    {
        return userPointsRecordsMapper.deleteUserPointsRecordsByIds(ids);
    }

    /**
     * 删除用户积分详情信息
     * 
     * @param id 用户积分详情主键
     * @return 结果
     */
    @Override
    public int deleteUserPointsRecordsById(Long id)
    {
        return userPointsRecordsMapper.deleteUserPointsRecordsById(id);
    }
}
