package cn.net.wenxin.system.service;

import java.util.List;
import cn.net.wenxin.system.domain.Role;

/**
 * 客户角色表Service接口
 * 
 * @author client
 * @date 2023-06-15
 */
public interface IRoleService
{
    /**
     * 查询客户角色表
     * 
     * @param id 客户角色表主键
     * @return 客户角色表
     */
    public Role selectTbRoleById(String id);

    /**
     * 查询客户角色表列表
     * 
     * @param tbRole 客户角色表
     * @return 客户角色表集合
     */
    public List<Role> selectTbRoleList(Role tbRole);

    /**
     * 新增客户角色表
     * 
     * @param tbRole 客户角色表
     * @return 结果
     */
    public int insertTbRole(Role tbRole);

    /**
     * 修改客户角色表
     * 
     * @param tbRole 客户角色表
     * @return 结果
     */
    public int updateTbRole(Role tbRole);

    /**
     * 批量删除客户角色表
     * 
     * @param ids 需要删除的客户角色表主键集合
     * @return 结果
     */
    public int deleteTbRoleByIds(String[] ids);

    /**
     * 删除客户角色表信息
     * 
     * @param id 客户角色表主键
     * @return 结果
     */
    public int deleteTbRoleById(String id);
}
