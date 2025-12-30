package cn.net.wenxin.system.mapper;


import cn.net.wenxin.system.domain.RoleUser;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色用户Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-06-13
 */
public interface RoleUserMapper
{
    /**
     * 查询角色用户
     * 
     * @param id 角色用户主键
     * @return 角色用户
     */
    public RoleUser selectTbRoleUserById(String id);

    /**
     * 查询角色用户列表
     * 
     * @param tbRoleUser 角色用户
     * @return 角色用户集合
     */
    public List<RoleUser> selectTbRoleUserList(RoleUser tbRoleUser);

    /**
     * 新增角色用户
     * 
     * @param tbRoleUser 角色用户
     * @return 结果
     */
    public int insertTbRoleUser(RoleUser tbRoleUser);

    /**
     * 修改角色用户
     * 
     * @param tbRoleUser 角色用户
     * @return 结果
     */
    public int updateTbRoleUser(RoleUser tbRoleUser);

    /**
     * 删除角色用户
     * 
     * @param id 角色用户主键
     * @return 结果
     */
    public int deleteTbRoleUserById(String id);

    /**
     * 批量删除角色用户
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTbRoleUserByIds(String[] ids);

    @Select("select role_id from tb_role_user where user_id = #{userId}")
    public List<String> getRoleByUserId(@Param("userId") String userId);

    public int batchRoleUser(List<RoleUser> list);

    @Delete("delete from tb_role_user where user_id = #{userId}")
    public void deleteUserRoleByUserId(@Param("userId") String userId);
}
