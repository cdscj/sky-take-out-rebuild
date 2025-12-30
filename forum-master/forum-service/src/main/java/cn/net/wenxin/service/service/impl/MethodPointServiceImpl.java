package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.service.domain.MethodPoint;
import cn.net.wenxin.service.mapper.MethodPointMapper;
import cn.net.wenxin.service.service.IMethodPointService;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @ClassName: MethodPointServiceImpl
 * @Description:
 * @Author forum.wenxin.net.cn
 * @Date 2023/12/13 15:32
 */
@Service
public class MethodPointServiceImpl implements IMethodPointService
{
    @Autowired
    private MethodPointMapper methodPointMapper;

    /**
     * 查询方法积分配置
     *
     * @param id 方法积分配置主键
     * @return 方法积分配置
     */
    @Override
    public MethodPoint selectMethodPointById(Long id)
    {
        return methodPointMapper.selectMethodPointById(id);
    }

    /**
     * 查询方法积分配置列表
     *
     * @param methodPoint 方法积分配置
     * @return 方法积分配置
     */
    @Override
    public List<MethodPoint> selectMethodPointList(MethodPoint methodPoint)
    {
        return methodPointMapper.selectMethodPointList(methodPoint);
    }

    /**
     * 新增方法积分配置
     *
     * @param methodPoint 方法积分配置
     * @return 结果
     */
    @Override
    public int insertMethodPoint(MethodPoint methodPoint)
    {
        methodPoint.setCreateTime(DateUtils.getNowDate());
        return methodPointMapper.insertMethodPoint(methodPoint);
    }

    /**
     * 修改方法积分配置
     *
     * @param methodPoint 方法积分配置
     * @return 结果
     */
    @Override
    public int updateMethodPoint(MethodPoint methodPoint)
    {
        methodPoint.setUpdateTime(DateUtils.getNowDate());
        return methodPointMapper.updateMethodPoint(methodPoint);
    }

    /**
     * 批量删除方法积分配置
     *
     * @param ids 需要删除的方法积分配置主键
     * @return 结果
     */
    @Override
    public int deleteMethodPointByIds(Long[] ids)
    {
        return methodPointMapper.deleteMethodPointByIds(ids);
    }

    /**
     * 删除方法积分配置信息
     *
     * @param id 方法积分配置主键
     * @return 结果
     */
    @Override
    public int deleteMethodPointById(Long id)
    {
        return methodPointMapper.deleteMethodPointById(id);
    }
}

