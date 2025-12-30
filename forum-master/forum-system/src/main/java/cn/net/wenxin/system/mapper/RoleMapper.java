package cn.net.wenxin.system.mapper;

import cn.net.wenxin.system.domain.Role;

import java.util.List;

/**
 * 角色表Mapper接口
 * 
 * @author client
 * @date 2023-06-13
 */
public interface RoleMapper
{
    /**
     * 查询角色表
     * 
     * @param id 角色表主键
     * @return 角色表
     */
    public Role selectTbRoleById(String id);

    /**
     * 查询角色表列表
     * 
     * @param tbRole 角色表
     * @return 角色表集合
     */
    public List<Role> selectTbRoleList(Role tbRole);

    /**
     * 新增角色表
     * 
     * @param tbRole 角色表
     * @return 结果
     */
    public int insertTbRole(Role tbRole);

    /**
     * 修改角色表
     * 
     * @param tbRole 角色表
     * @return 结果
     */
    public int updateTbRole(Role tbRole);

    /**
     * 删除角色表
     * 
     * @param id 角色表主键
     * @return 结果
     */
    public int deleteTbRoleById(String id);

    /**
     * 批量删除角色表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbRoleByIds(String[] ids);
}
