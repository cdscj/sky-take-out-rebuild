package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 主题回复举报对象 TopicReplyReport
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class TopicReplyReport extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表id */
    private Long id;

    /** 主题ID */
    @Excel(name = "主题ID")
    private Long topicId;

    /** 回复表ID，为null则是举报主题 */
    @Excel(name = "回复表ID，为null则是举报主题")
    private Long replyId;

    /** 举报人 */
    @Excel(name = "举报人")
    private String reportUser;

    /** 举报说明 */
    @Excel(name = "举报说明")
    private String reportExplain;

    /** 处理状态：1未处理，已处理 */
    @Excel(name = "处理状态：1未处理，已处理")
    private String status;

    /** 处理说明 */
    @Excel(name = "处理说明")
    private String reportResult;

    private Integer resultStatus;

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
    public void setTopicId(Long topicId) 
    {
        this.topicId = topicId;
    }

    public Long getTopicId() 
    {
        return topicId;
    }
    public void setReplyId(Long replyId) 
    {
        this.replyId = replyId;
    }

    public Long getReplyId() 
    {
        return replyId;
    }
    public void setReportUser(String reportUser) 
    {
        this.reportUser = reportUser;
    }

    public String getReportUser() 
    {
        return reportUser;
    }
    public void setReportExplain(String reportExplain) 
    {
        this.reportExplain = reportExplain;
    }

    public String getReportExplain() 
    {
        return reportExplain;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setReportResult(String reportResult) 
    {
        this.reportResult = reportResult;
    }

    public String getReportResult() 
    {
        return reportResult;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public Integer getResultStatus() {
        return resultStatus;
    }

    public void setResultStatus(Integer resultStatus) {
        this.resultStatus = resultStatus;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("topicId", getTopicId())
            .append("replyId", getReplyId())
            .append("reportUser", getReportUser())
            .append("reportExplain", getReportExplain())
            .append("status", getStatus())
            .append("reportResult", getReportResult())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
