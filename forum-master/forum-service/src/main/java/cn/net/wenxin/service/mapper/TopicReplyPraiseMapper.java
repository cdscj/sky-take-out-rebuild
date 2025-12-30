package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.TopicReplyPraise;
import org.apache.ibatis.annotations.Param;

/**
 * 主题回复点赞Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface TopicReplyPraiseMapper 
{
    /**
     * 查询主题回复点赞
     * 
     * @param id 主题回复点赞主键
     * @return 主题回复点赞
     */
    public TopicReplyPraise selectTopicReplyPraiseById(Long id);

    /**
     * 查询主题回复点赞列表
     * 
     * @param topicReplyPraise 主题回复点赞
     * @return 主题回复点赞集合
     */
    public List<TopicReplyPraise> selectTopicReplyPraiseList(TopicReplyPraise topicReplyPraise);

    /**
     * 新增主题回复点赞
     * 
     * @param topicReplyPraise 主题回复点赞
     * @return 结果
     */
    public int insertTopicReplyPraise(TopicReplyPraise topicReplyPraise);

    /**
     * 修改主题回复点赞
     * 
     * @param topicReplyPraise 主题回复点赞
     * @return 结果
     */
    public int updateTopicReplyPraise(TopicReplyPraise topicReplyPraise);

    /**
     * 删除主题回复点赞
     * 
     * @param id 主题回复点赞主键
     * @return 结果
     */
    public int deleteTopicReplyPraiseById(Long id);

    /**
     * 批量删除主题回复点赞
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicReplyPraiseByIds(Long[] ids);

    /**
     * 取消点赞
     * @param praise
     * @return
     */
    public int unpraise(TopicReplyPraise praise);

    /**
     * 是否点赞主题
     * @param topicId
     * @param userName
     * @return
     */
    public int selectPraiseByTopicIdAndUser(@Param("topicId") Long topicId, @Param("userName")String userName);

}
