package cn.net.wenxin.service.service.impl;

import java.util.Date;
import java.util.List;

import cn.net.wenxin.service.domain.UserPointsRecords;
import cn.net.wenxin.service.mapper.UserPointsRecordsMapper;
import cn.net.wenxin.common.enums.PointsType;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.DictUtils;
import cn.net.wenxin.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.UserPointsMapper;
import cn.net.wenxin.service.domain.UserPoints;
import cn.net.wenxin.service.service.IUserPointsService;

import static cn.net.wenxin.common.utils.DictUtils.getDictValue;

/**
 * 用户积分Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class UserPointsServiceImpl implements IUserPointsService 
{
    @Autowired
    private UserPointsMapper userPointsMapper;
    @Autowired
    private UserPointsRecordsMapper userPointsRecordsMapper;

    /**
     * 查询用户积分
     * 
     * @param id 用户积分主键
     * @return 用户积分
     */
    @Override
    public UserPoints selectUserPointsById(Long id)
    {
        return userPointsMapper.selectUserPointsById(id);
    }

    /**
     * 查询用户积分列表
     * 
     * @param userPoints 用户积分
     * @return 用户积分
     */
    @Override
    public List<UserPoints> selectUserPointsList(UserPoints userPoints)
    {
        return userPointsMapper.selectUserPointsList(userPoints);
    }

    /**
     * 新增用户积分
     * 
     * @param userPoints 用户积分
     * @return 结果
     */
    @Override
    public int insertUserPoints(UserPoints userPoints)
    {
        userPoints.setCreateTime(DateUtils.getNowDate());
        return userPointsMapper.insertUserPoints(userPoints);
    }

    /**
     * 修改用户积分
     * 
     * @param userPoints 用户积分
     * @return 结果
     */
    @Override
    public int updateUserPoints(UserPoints userPoints)
    {
        userPoints.setUpdateTime(DateUtils.getNowDate());
        return userPointsMapper.updateUserPoints(userPoints);
    }

    /**
     * 批量删除用户积分
     * 
     * @param ids 需要删除的用户积分主键
     * @return 结果
     */
    @Override
    public int deleteUserPointsByIds(Long[] ids)
    {
        return userPointsMapper.deleteUserPointsByIds(ids);
    }

    /**
     * 删除用户积分信息
     * 
     * @param id 用户积分主键
     * @return 结果
     */
    @Override
    public int deleteUserPointsById(Long id)
    {
        return userPointsMapper.deleteUserPointsById(id);
    }

    @Override
    public void addUserPoints(String userId, String type,Long rid) {
        //处理用户积分逻辑
        if(StringUtils.isBlank(userId)||StringUtils.isBlank(type)|| rid == null){
            return;
        }
        String rName = PointsType.getInfo(type);
        String dictPoints = DictUtils.getDictValue("type_points",type);
        long points = 0L;
        if(StringUtils.isNotBlank(dictPoints)){
            points = Long.parseLong(dictPoints);
        }

        UserPoints userPoints = userPointsMapper.selectUserPointsByUserId(userId);
        if(userPoints == null){
            userPoints = new UserPoints();
            userPoints.setUserId(userId);
            userPoints.setCurrentPoint(points);
            userPoints.setCreateBy(userId);
            userPoints.setCreateTime(new Date());
            userPoints.setUpdateBy(userId);
            userPoints.setUpdateTime(new Date());
            userPointsMapper.insertUserPoints(userPoints);
        }else{
            long currentPoint = points+userPoints.getCurrentPoint().longValue();
            userPoints.setCurrentPoint(currentPoint);
            userPoints.setUpdateBy(userId);
            userPoints.setUpdateTime(new Date());
            userPointsMapper.updateUserPoints(userPoints);
        }

        UserPointsRecords records = new UserPointsRecords();
        records.setPointsId(userPoints.getId());
        records.setType(type);
        records.setPoints(points);
        records.setRelevanceTable(rName);
        records.setRelevanceId(rid);
        records.setCreateBy(userId);
        records.setCreateTime(new Date());
        records.setUpdateBy(userId);
        records.setUpdateTime(new Date());
        userPointsRecordsMapper.insertUserPointsRecords(records);
    }

}
