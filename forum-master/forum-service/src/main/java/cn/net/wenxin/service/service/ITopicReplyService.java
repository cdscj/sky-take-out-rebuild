package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.TopicReply;
import cn.net.wenxin.service.domain.vo.TopicReplyVo;

/**
 * 主题回复记录Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface ITopicReplyService 
{
    /**
     * 查询主题回复记录
     * 
     * @param id 主题回复记录主键
     * @return 主题回复记录
     */
    public TopicReply selectTopicReplyById(Long id);

    /**
     * 查询主题回复记录列表
     * 
     * @param replyVo 主题回复记录
     * @return 主题回复记录集合
     */
    public List<TopicReplyVo> selectTopicReplyList(TopicReplyVo replyVo);

    /**
     * 新增主题回复记录
     * 
     * @param topicReply 主题回复记录
     * @return 结果
     */
    public int insertTopicReply(TopicReply topicReply);

    /**
     * 修改主题回复记录
     * 
     * @param topicReply 主题回复记录
     * @return 结果
     */
    public int updateTopicReply(TopicReply topicReply);

    /**
     * 批量删除主题回复记录
     * 
     * @param ids 需要删除的主题回复记录主键集合
     * @return 结果
     */
    public int deleteTopicReplyByIds(Long[] ids);

    /**
     * 删除主题回复记录信息
     * 
     * @param id 主题回复记录主键
     * @return 结果
     */
    public int deleteTopicReplyById(Long id);

    /**
     * 获取针对主题的回复列表
     * @param topicId
     * @return
     */
    public List<TopicReply> selectTopicReplyMainList(Long topicId);

    /**
     * 获取评论下回复
     * @param replyId
     * @return
     */
    public List<TopicReply> getTopicReplyList(Long replyId);
}
