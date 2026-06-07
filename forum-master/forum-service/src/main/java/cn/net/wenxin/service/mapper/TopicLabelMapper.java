package cn.net.wenxin.service.mapper;

import java.util.List;

import cn.net.wenxin.service.domain.TopicLabel;
import cn.net.wenxin.service.domain.vo.TopicLabelVo;
import org.apache.ibatis.annotations.Param;

/**
 * 主题标签Mapper接口
 *
 * @author forum.wenxin.net.cn
 * @date 2023-08-25
 */
public interface TopicLabelMapper
{
    /**
     * 查询主题标签
     *
     * @param topicId 主题标签主键
     * @return 主题标签
     */
    public TopicLabel selectTopicLabelByTopicId(Long topicId);

    /**
     * 查询主题标签列表
     *
     * @param topicLabel 主题标签
     * @return 主题标签集合
     */
    public List<TopicLabel> selectTopicLabelList(TopicLabel topicLabel);

    /**
     * 新增主题标签
     *
     * @param topicLabel 主题标签
     * @return 结果
     */
    public int insertTopicLabel(TopicLabel topicLabel);

    /**
     * 修改主题标签
     *
     * @param topicLabel 主题标签
     * @return 结果
     */
    public int updateTopicLabel(TopicLabel topicLabel);

    /**
     * 删除主题标签
     *
     * @param topicId 主题标签主键
     * @return 结果
     */
    public int deleteTopicLabelByTopicId(Long topicId);

    /**
     * 批量删除主题标签
     *
     * @param topicIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicLabelByTopicIds(Long[] topicIds);

    List<TopicLabelVo> selectTopicLabelVoList(Long topicId);

    /**
     * 批量按话题 ID 查询标签（用于消除 N+1 查询）
     */
    List<TopicLabelVo> selectTopicLabelVoListByTopicIds(@Param("topicIds") List<Long> topicIds);
}
