package cn.net.wenxin.service.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * 主题历史对象 tb_topic_version
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class TopicVersion extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键id */
    private Long id;

    /** 主题ID */
    @Excel(name = "主题ID")
    private Long topicId;

    /** 版本类型：1新增，2编辑 */
    @Excel(name = "版本类型：1新增，2编辑")
    private String type;

    /** 审核人 */
    @Excel(name = "审核人")
    private String auditUser;

    /** 审核时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "审核时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date auditTime;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 审核结果：1通过，2不通过 */
    @Excel(name = "审核结果：1通过，2不通过")
    private String auditStatus;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setTopicId(Long topicId)
    {
        this.topicId = topicId;
    }

    public Long getTopicId()
    {
        return topicId;
    }
    public void setType(String type) 
    {
        this.type = type;
    }

    public String getType() 
    {
        return type;
    }
    public void setAuditUser(String auditUser) 
    {
        this.auditUser = auditUser;
    }

    public String getAuditUser() 
    {
        return auditUser;
    }
    public void setAuditTime(Date auditTime) 
    {
        this.auditTime = auditTime;
    }

    public Date getAuditTime() 
    {
        return auditTime;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setAuditStatus(String auditStatus) 
    {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatus() 
    {
        return auditStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("topicId", getTopicId())
            .append("type", getType())
            .append("auditUser", getAuditUser())
            .append("auditTime", getAuditTime())
            .append("delFlag", getDelFlag())
            .append("auditStatus", getAuditStatus())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
