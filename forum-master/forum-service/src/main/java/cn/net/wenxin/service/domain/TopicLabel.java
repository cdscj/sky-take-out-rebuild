package cn.net.wenxin.service.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 主题标签对象 tb_topic_label
 *
 * @author forum.wenxin.net.cn
 * @date 2023-08-25
 */
public class TopicLabel {
    private static final long serialVersionUID = 1L;

    /**
     * 主题ID
     */
    private Long topicId;

    /**
     * 标签ID
     */
    private Long labelId;

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setLabelId(Long labelId) {
        this.labelId = labelId;
    }

    public Long getLabelId() {
        return labelId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder

                (this, ToStringStyle.MULTI_LINE_STYLE)
                .append("topicId", getTopicId())
                .append("labelId", getLabelId())
                .toString();
    }
}
