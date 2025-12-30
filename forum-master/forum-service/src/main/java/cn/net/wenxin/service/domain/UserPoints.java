package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户积分对象 tb_user_points
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class UserPoints extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表ID */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userId;

    /** 当前分数 */
    @Excel(name = "当前分数")
    private Long currentPoint;

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
    public void setUserId(String userId) 
    {
        this.userId = userId;
    }

    public String getUserId() 
    {
        return userId;
    }
    public void setCurrentPoint(Long currentPoint) 
    {
        this.currentPoint = currentPoint;
    }

    public Long getCurrentPoint() 
    {
        return currentPoint;
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
            .append("userId", getUserId())
            .append("currentPoint", getCurrentPoint())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
