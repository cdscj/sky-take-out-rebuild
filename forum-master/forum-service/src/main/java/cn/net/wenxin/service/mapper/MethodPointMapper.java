package cn.net.wenxin.service.mapper;

import cn.net.wenxin.service.domain.MethodPoint;

import java.util.List;

/**
 * @ClassName: MethodPointMapper
 * @Description:
 * @Author forum.wenxin.net.cn
 * @Date 2023/12/13 15:33
 */
public interface MethodPointMapper
{
    /**
     * 查询方法积分配置
     *
     * @param id 方法积分配置主键
     * @return 方法积分配置
     */
    public MethodPoint selectMethodPointById(Long id);

    /**
     * 查询方法积分配置列表
     *
     * @param methodPoint 方法积分配置
     * @return 方法积分配置集合
     */
    public List<MethodPoint> selectMethodPointList(MethodPoint methodPoint);

    /**
     * 新增方法积分配置
     *
     * @param methodPoint 方法积分配置
     * @return 结果
     */
    public int insertMethodPoint(MethodPoint methodPoint);

    /**
     * 修改方法积分配置
     *
     * @param methodPoint 方法积分配置
     * @return 结果
     */
    public int updateMethodPoint(MethodPoint methodPoint);

    /**
     * 删除方法积分配置
     *
     * @param id 方法积分配置主键
     * @return 结果
     */
    public int deleteMethodPointById(Long id);

    /**
     * 批量删除方法积分配置
     *
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteMethodPointByIds(Long[] ids);
}

