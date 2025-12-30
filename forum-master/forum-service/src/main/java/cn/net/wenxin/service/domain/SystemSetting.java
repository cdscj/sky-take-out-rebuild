package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 系统设置对象 tb_system_setting
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class SystemSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签id */
    private Long id;

    /** 一级编号 */
    @Excel(name = "一级编号")
    private String keyCode;

    /** 二级编号 */
    @Excel(name = "二级编号")
    private String valueCode;

    /** 系统值 */
    @Excel(name = "系统值")
    private String value;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setKeyCode(String keyCode) 
    {
        this.keyCode = keyCode;
    }

    public String getKeyCode() 
    {
        return keyCode;
    }
    public void setValueCode(String valueCode) 
    {
        this.valueCode = valueCode;
    }

    public String getValueCode() 
    {
        return valueCode;
    }
    public void setValue(String value) 
    {
        this.value = value;
    }

    public String getValue() 
    {
        return value;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("keyCode", getKeyCode())
            .append("valueCode", getValueCode())
            .append("value", getValue())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
