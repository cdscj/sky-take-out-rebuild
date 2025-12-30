package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

/**
 * 主题对象 tb_topic_info
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class TopicInfo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主题id */
    private Long id;

    /** 主题标题 */
    @Excel(name = "主题标题")
    private String title;

    /** 主题内容 */
    @Excel(name = "主题内容")
    private String topicContent;

    /** 主题状态(0草稿,1审核中,2已发布,3审核不通过,4撤销) */
    @Excel(name = "主题状态(0草稿,1审核中,2已发布,3审核不通过,4撤销)")
    private String status;

    List<Long> labelIds;

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
    public void setTitle(String title) 
    {
        this.title = title;
    }

    public String getTitle() 
    {
        return title;
    }
    public void setTopicContent(String topicContent)
    {
        this.topicContent = topicContent;
    }

    public String getTopicContent()
    {
        return topicContent;
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

    public List<Long> getLabelIds() {
        return labelIds;
    }

    public void setLabelIds(List<Long> labelIds) {
        this.labelIds = labelIds;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("title", getTitle())
            .append("topicContent", getTopicContent())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
