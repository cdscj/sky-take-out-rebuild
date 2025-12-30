package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 级别对象 tb_level
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class Level extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签id */
    private Long id;

    /** 级别名称 */
    @Excel(name = "级别名称")
    private String name;

    /** 级别图标地址 */
    @Excel(name = "级别图标地址")
    private String icon;

    /** 所属级别最小积分 */
    @Excel(name = "所属级别最小积分")
    private Long pointsMin;

    /** 所属级别最大积分 */
    @Excel(name = "所属级别最大积分")
    private Long pointsMax;

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
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setIcon(String icon) 
    {
        this.icon = icon;
    }

    public String getIcon() 
    {
        return icon;
    }
    public void setPointsMin(Long pointsMin) 
    {
        this.pointsMin = pointsMin;
    }

    public Long getPointsMin() 
    {
        return pointsMin;
    }
    public void setPointsMax(Long pointsMax) 
    {
        this.pointsMax = pointsMax;
    }

    public Long getPointsMax() 
    {
        return pointsMax;
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
            .append("name", getName())
            .append("icon", getIcon())
            .append("pointsMin", getPointsMin())
            .append("pointsMax", getPointsMax())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
