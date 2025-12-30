package cn.net.wenxin.system.mapper;


import cn.net.wenxin.system.domain.Dept;

import java.util.List;

/**
 * 客户部门Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-06-13
 */
public interface DeptMapper
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
     * 删除客户部门
     * 
     * @param id 客户部门主键
     * @return 结果
     */
    public int deleteTbDeptById(String id);

    /**
     * 批量删除客户部门
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbDeptByIds(String[] ids);

    public Dept selectDeptByCode(String code);

}
