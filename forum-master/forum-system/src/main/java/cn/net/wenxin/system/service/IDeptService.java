package cn.net.wenxin.system.service;

import java.util.List;
import cn.net.wenxin.system.domain.Dept;

/**
 * 客户部门Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-06-15
 */
public interface IDeptService
{
    /**
     * 查询客户部门
     * 
     * @param id 客户部门主键
     * @return 客户部门
     */
    public Dept selectTbDeptById(String id);

    /**
     * 查询客户部门列表
     * 
     * @param tbDept 客户部门
     * @return 客户部门集合
     */
    public List<Dept> selectTbDeptList(Dept tbDept);

    /**
     * 新增客户部门
     * 
     * @param tbDept 客户部门
     * @return 结果
     */
    public int insertTbDept(Dept tbDept);

    /**
     * 修改客户部门
     * 
     * @param tbDept 客户部门
     * @return 结果
     */
    public int updateTbDept(Dept tbDept);

    /**
     * 批量删除客户部门
     * 
     * @param ids 需要删除的客户部门主键集合
     * @return 结果
     */
    public int deleteTbDeptByIds(String[] ids);

    /**
     * 删除客户部门信息
     * 
     * @param id 客户部门主键
     * @return 结果
     */
    public int deleteTbDeptById(String id);
}
