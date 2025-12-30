package cn.net.wenxin.service.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 主题回复举报对象 TopicReplyReport
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-12-25
 */
public class TopicReplyReportVo
{

    /** 表id */
    private Long id;

    /** 举报类型*/
    private Integer type;

    /** 主题ID */
    private Long topicId;

    private String topicTitle;

    private String topicContent;

    /** 回复表ID，为null则是举报主题 */
    private Long replyId;

    private String replyContent;

    private Long reportNum;

    /** 举报人 */
    private String reportUser;

    /** 举报说明 */
    private String reportExplain;

    /** 处理状态：1未处理，已处理 */
    private String status;

    private Integer resultStatus;

    /** 处理说明 */
    private String reportResult;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    private String updateBy;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

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

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getReplyContent() {
        return replyContent;
    }

    public void setReplyContent(String replyContent) {
        this.replyContent = replyContent;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getReportNum() {
        return reportNum;
    }

    public void setReportNum(Long reportNum) {
        this.reportNum = reportNum;
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
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
