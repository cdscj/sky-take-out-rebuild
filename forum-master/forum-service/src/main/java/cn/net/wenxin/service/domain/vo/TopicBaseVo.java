package cn.net.wenxin.service.domain.vo;

/**
 * @ClassName: TopicDateilVo
 * @Description:
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/25 16:37
 */

public class TopicBaseVo{

    /** 主题id */
    private Long id;

    /** 主题标题 */
    private String title;

    /** 主题内容 */
    private String topicContent;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }

}
