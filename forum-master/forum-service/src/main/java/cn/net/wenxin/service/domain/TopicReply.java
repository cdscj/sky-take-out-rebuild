package cn.net.wenxin.service.domain;

import cn.net.wenxin.service.domain.vo.UserVo;
import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 主题回复记录对象 tb_topic_reply
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class TopicReply extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表id */
    private Long id;

    /** 主题id */
    @Excel(name = "主题id")
    private Long topicId;

    private Long replyMainId;

    /** 回复表ID */
    @Excel(name = "回复表ID")
    private Long replyId;

    /** 回复人ID */
    @Excel(name = "回复人ID")
    private String replyerId;

    /** 回复内容 */
    @Excel(name = "回复内容")
    private String replyContent;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;
    /** 1是主评论 */
    private Integer type;

    /**
     * 是否赞：0否，1是
     */
    private Integer praise;

    private UserVo userVo;

    private UserVo replyUser;

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

    public Long getReplyMainId() {
        return replyMainId;
    }

    public void setReplyMainId(Long replyMainId) {
        this.replyMainId = replyMainId;
    }

    public UserVo getUserVo() {
        return userVo;
    }

    public void setUserVo(UserVo userVo) {
        this.userVo = userVo;
    }

    public UserVo getReplyUser() {
        return replyUser;
    }

    public void setReplyUser(UserVo replyUser) {
        this.replyUser = replyUser;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("topicId", getTopicId())
            .append("replyId", getReplyId())
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
