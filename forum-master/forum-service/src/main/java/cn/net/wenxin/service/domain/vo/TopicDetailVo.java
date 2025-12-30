package cn.net.wenxin.service.domain.vo;

import cn.net.wenxin.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: TopicDateilVo
 * @Description:
 * @Author forum.wenxin.net.cn
 * @Date 2023/8/25 16:37
 */

public class TopicDetailVo extends BaseEntity {

    /** 主题id */
    private Long id;

    /** 主题标题 */
    private String title;

    /** 主题内容 */
    private String topicContent;

    /** 主题状态(0草稿,1审核中,2已发布,3审核不通过,4撤销) */
    private String status;

    /**
     * 创建人相关信息
     */
    private UserVo createUser;

    /**
     * 主题标签
     */
    List<TopicLabelVo> topicLabelVoList;

    /**
     * 最新回复时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date latestReplyDate;

    /**
     * 最新回复时间说明
     */
    private String latestReplyDateName;

    /**
     * 回复数量
     */
    private Integer replyNum;

    /**
     * 访问量
     */
    private Integer visitNum;

    /**
     * 是否关注：0否，1是
     */
    private Integer follow;

    /**
     * 是否赞：0否，1是
     */
    private Integer praise;

    /**
     * 历史列表
     */
    private List<TopicVersionVo> versionVos;


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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserVo getCreateUser() {
        return createUser;
    }

    public void setCreateUser(UserVo createUser) {
        this.createUser = createUser;
    }

    public List<TopicLabelVo> getTopicLabelVoList() {
        return topicLabelVoList;
    }

    public void setTopicLabelVoList(List<TopicLabelVo> topicLabelVoList) {
        this.topicLabelVoList = topicLabelVoList;
    }

    public Date getLatestReplyDate() {
        return latestReplyDate;
    }

    public void setLatestReplyDate(Date latestReplyDate) {
        this.latestReplyDate = latestReplyDate;
    }

    public String getLatestReplyDateName() {
        return latestReplyDateName;
    }

    public void setLatestReplyDateName(String latestReplyDateName) {
        this.latestReplyDateName = latestReplyDateName;
    }

    public Integer getReplyNum() {
        return replyNum;
    }

    public void setReplyNum(Integer replyNum) {
        this.replyNum = replyNum;
    }

    public Integer getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(Integer visitNum) {
        this.visitNum = visitNum;
    }

    public List<TopicVersionVo> getVersionVos() {
        return versionVos;
    }

    public void setVersionVos(List<TopicVersionVo> versionVos) {
        this.versionVos = versionVos;
    }

    public Integer getFollow() {
        return follow;
    }

    public void setFollow(Integer follow) {
        this.follow = follow;
    }

    public Integer getPraise() {
        return praise;
    }

    public void setPraise(Integer praise) {
        this.praise = praise;
    }
}
