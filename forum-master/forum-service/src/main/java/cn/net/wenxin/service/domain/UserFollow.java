package cn.net.wenxin.service.domain;

/**
 * @ClassName: UserFollow
 * @Description:
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/28 17:36
 */

public class UserFollow {

    private Long topicId;

    private String userId;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
