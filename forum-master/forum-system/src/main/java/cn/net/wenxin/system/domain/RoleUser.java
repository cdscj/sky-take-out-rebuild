package cn.net.wenxin.system.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 角色用户对象 tb_role_user
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-06-13
 */
public class RoleUser extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private String id;

    /** 角色ID */
    @Excel(name = "角色ID")
    private String roleId;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    public void setId(String id) 
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setRoleId(String roleId) 
    {
        this.roleId = roleId;
    }

    public String getRoleId() 
    {
        return roleId;
    }
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("roleId", getRoleId())
            .append("userId", getUserId())
            .toString();
    }
}
