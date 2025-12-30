package cn.net.wenxin.system.service.impl;

import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.uuid.IdUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.system.mapper.RoleMapper;
import cn.net.wenxin.system.domain.Role;
import cn.net.wenxin.system.service.IRoleService;

/**
 * 客户角色表Service业务层处理
 * 
 * @author client
 * @date 2023-06-15
 */
@Service
public class RoleServiceImpl implements IRoleService
{
    @Autowired
    private RoleMapper tbRoleMapper;

    /**
     * 查询客户角色表
     * 
     * @param id 客户角色表主键
     * @return 客户角色表
     */
    @Override
    public Role selectTbRoleById(String id)
    {
        return tbRoleMapper.selectTbRoleById(id);
    }

    /**
     * 查询客户角色表列表
     * 
     * @param tbRole 客户角色表
     * @return 客户角色表
     */
    @Override
    public List<Role> selectTbRoleList(Role tbRole)
    {
        return tbRoleMapper.selectTbRoleList(tbRole);
    }

    /**
     * 新增客户角色表
     * 
     * @param tbRole 客户角色表
     * @return 结果
     */
    @Override
    public int insertTbRole(Role tbRole)
    {
        tbRole.setId(IdUtils.fastSimpleUUID());
        tbRole.setCreateTime(DateUtils.getNowDate());
        return tbRoleMapper.insertTbRole(tbRole);
    }

    /**
     * 修改客户角色表
     * 
     * @param tbRole 客户角色表
     * @return 结果
     */
    @Override
    public int updateTbRole(Role tbRole)
    {
        tbRole.setUpdateTime(DateUtils.getNowDate());
        return tbRoleMapper.updateTbRole(tbRole);
    }

    /**
     * 批量删除客户角色表
     * 
     * @param ids 需要删除的客户角色表主键
     * @return 结果
     */
    @Override
    public int deleteTbRoleByIds(String[] ids)
    {
        return tbRoleMapper.deleteTbRoleByIds(ids);
    }

    /**
     * 删除客户角色表信息
     * 
     * @param id 客户角色表主键
     * @return 结果
     */
    @Override
    public int deleteTbRoleById(String id)
    {
        return tbRoleMapper.deleteTbRoleById(id);
    }
}
