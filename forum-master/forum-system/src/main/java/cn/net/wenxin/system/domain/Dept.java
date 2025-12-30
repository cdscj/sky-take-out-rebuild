package cn.net.wenxin.system.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 客户部门对象 tb_dept
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-06-13
 */
public class Dept extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private String id;

    /** 部门编号 */
    @Excel(name = "部门编号")
    private String code;

    /** 部门名称 */
    @Excel(name = "部门名称")
    private String name;

    /** 父部门编号 */
    @Excel(name = "父部门编号")
    private String pcode;

    /** 部门路由 */
    @Excel(name = "部门路由")
    private String path;

    public void setId(String id)
    {
        this.id = id;
    }

    public String getId() 
    {
        return id;
    }
    public void setCode(String code) 
    {
        this.code = code;
    }

    public String getCode() 
    {
        return code;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setPcode(String pcode) 
    {
        this.pcode = pcode;
    }

    public String getPcode() 
    {
        return pcode;
    }
    public void setPath(String path) 
    {
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("code", getCode())
            .append("name", getName())
            .append("pcode", getPcode())
            .append("path", getPath())
            .append("remark", getRemark())
            .append("createBy", getCreateBy())
            .append("craeteTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
