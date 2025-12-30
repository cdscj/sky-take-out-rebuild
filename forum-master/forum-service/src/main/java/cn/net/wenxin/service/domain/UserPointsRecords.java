package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 用户积分详情对象 tb_user_points_records
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class UserPointsRecords extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表ID */
    private Long id;

    /** 用户积分表ID */
    @Excel(name = "用户积分表ID")
    private Long pointsId;

    /** 积分类型：1发帖，2回帖，3举报，4赞，5关注 */
    @Excel(name = "积分类型：1发帖，2回帖，3举报，4赞，5关注")
    private String type;

    /** 获得分数 */
    @Excel(name = "获得分数")
    private Long points;

    /** 关联表名 */
    @Excel(name = "关联表名")
    private String relevanceTable;

    /** 关联表ID */
    @Excel(name = "关联表ID")
    private Long relevanceId;

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
    public void setPointsId(Long pointsId) 
    {
        this.pointsId = pointsId;
    }

    public Long getPointsId() 
    {
        return pointsId;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setPoints(Long points) 
    {
        this.points = points;
    }

    public Long getPoints() 
    {
        return points;
    }
    public void setRelevanceTable(String relevanceTable) 
    {
        this.relevanceTable = relevanceTable;
    }

    public String getRelevanceTable() 
    {
        return relevanceTable;
    }
    public void setRelevanceId(Long relevanceId) 
    {
        this.relevanceId = relevanceId;
    }

    public Long getRelevanceId() 
    {
        return relevanceId;
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
            .append("pointsId", getPointsId())
            .append("type", getType())
            .append("points", getPoints())
            .append("relevanceTable", getRelevanceTable())
            .append("relevanceId", getRelevanceId())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
