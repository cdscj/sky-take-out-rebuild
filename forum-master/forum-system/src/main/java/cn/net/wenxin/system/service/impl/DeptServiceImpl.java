package cn.net.wenxin.system.service.impl;

import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.system.mapper.DeptMapper;
import cn.net.wenxin.system.domain.Dept;
import cn.net.wenxin.system.service.IDeptService;

/**
 * 客户部门Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-06-15
 */
@Service
public class DeptServiceImpl implements IDeptService
{
    @Autowired
    private DeptMapper tbDeptMapper;

    /**
     * 查询客户部门
     * 
     * @param id 客户部门主键
     * @return 客户部门
     */
    @Override
    public Dept selectTbDeptById(String id)
    {
        return tbDeptMapper.selectTbDeptById(id);
    }

    /**
     * 查询客户部门列表
     * 
     * @param tbDept 客户部门
     * @return 客户部门
     */
    @Override
    public List<Dept> selectTbDeptList(Dept tbDept)
    {
        return tbDeptMapper.selectTbDeptList(tbDept);
    }

    /**
     * 新增客户部门
     * 
     * @param tbDept 客户部门
     * @return 结果
     */
    @Override
    public int insertTbDept(Dept tbDept)
    {
        tbDept.setId(IdUtils.fastSimpleUUID());
        tbDept.setCreateTime(DateUtils.getNowDate());
        return tbDeptMapper.insertTbDept(tbDept);
    }

    /**
     * 修改客户部门
     * 
     * @param tbDept 客户部门
     * @return 结果
     */
    @Override
    public int updateTbDept(Dept tbDept)
    {
        tbDept.setUpdateTime(DateUtils.getNowDate());
        return tbDeptMapper.updateTbDept(tbDept);
    }

    /**
     * 批量删除客户部门
     * 
     * @param ids 需要删除的客户部门主键
     * @return 结果
     */
    @Override
    public int deleteTbDeptByIds(String[] ids)
    {
        return tbDeptMapper.deleteTbDeptByIds(ids);
    }

    /**
     * 删除客户部门信息
     * 
     * @param id 客户部门主键
     * @return 结果
     */
    @Override
    public int deleteTbDeptById(String id)
    {
        return tbDeptMapper.deleteTbDeptById(id);
    }
}
