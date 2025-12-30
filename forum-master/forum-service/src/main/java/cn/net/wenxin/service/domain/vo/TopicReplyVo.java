package cn.net.wenxin.service.domain.vo;

import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 主题回复记录对象 tb_topic_reply
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class TopicReplyVo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表id */
    private Long id;

    /** 主题id */
    private Long topicId;

    private String topicTitle;

    private String topicContent;

    /** 回复人ID */
    private String replyerId;

    private String replyerName;

    /** 回复内容 */
    private String replyContent;

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

    public void setReplyerId(String replyerId) 
    {
        this.replyerId = replyerId;
    }

    public String getReplyerId() 
    {
        return replyerId;
    }
    public void setReplyContent(String replyContent) 
    {
        this.replyContent = replyContent;
    }

    public String getReplyContent() 
    {
        return replyContent;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

    public String getReplyerName() {
        return replyerName;
    }

    public void setReplyerName(String replyerName) {
        this.replyerName = replyerName;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("topicId", getTopicId())
            .append("replyerId", getReplyerId())
            .append("replyContent", getReplyContent())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
