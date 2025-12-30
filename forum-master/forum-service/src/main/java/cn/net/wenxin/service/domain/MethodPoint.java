package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * @ClassName: MethodPoint
 * @Description:
 * @Author forum.wenxin.net.cn
 * @Date 2023/12/13 15:30
 */
public class MethodPoint extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 方法名 */
    @Excel(name = "方法名")
    private String methodName;

    /** 方法路径 */
    @Excel(name = "方法路径")
    private String methodPath;

    /** 方法名称 */
    @Excel(name = "方法名称")
    private String name;

    /** 方法对应积分 */
    @Excel(name = "方法对应积分")
    private Long points;

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
    public void setMethodName(String methodName)
    {
        this.methodName = methodName;
    }

    public String getMethodName()
    {
        return methodName;
    }
    public void setMethodPath(String methodPath)
    {
        this.methodPath = methodPath;
    }

    public String getMethodPath()
    {
        return methodPath;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setPoints(Long points)
    {
        this.points = points;
    }

    public Long getPoints()
    {
        return points;
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
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("methodName", getMethodName())
                .append("methodPath", getMethodPath())
                .append("name", getName())
                .append("points", getPoints())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}

