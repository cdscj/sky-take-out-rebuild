package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.TopicReply;
import cn.net.wenxin.service.domain.vo.TopicReplyVo;

/**
 * 主题回复记录Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface TopicReplyMapper 
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
     * @param topicReply 主题回复记录
     * @return 主题回复记录集合
     */
    public List<TopicReply> selectTopicReplyList(TopicReply topicReply);

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
     * 删除主题回复记录
     * 
     * @param id 主题回复记录主键
     * @return 结果
     */
    public int deleteTopicReplyById(Long id);

    /**
     * 批量删除主题回复记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicReplyByIds(Long[] ids);

    public List<TopicReplyVo> selectTopicReplyVoList(TopicReplyVo replyVo);
}
