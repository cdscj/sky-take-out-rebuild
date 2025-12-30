package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.TopicVersion;
import cn.net.wenxin.service.domain.vo.TopicVersionVo;

/**
 * 主题历史Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface TopicVersionMapper 
{
    /**
     * 查询主题历史
     * 
     * @param id 主题历史主键
     * @return 主题历史
     */
    public TopicVersion selectTopicVersionById(Long id);

    /**
     * 查询主题历史列表
     * 
     * @param topicVersion 主题历史
     * @return 主题历史集合
     */
    public List<TopicVersion> selectTopicVersionList(TopicVersion topicVersion);

    /**
     * 新增主题历史
     * 
     * @param topicVersion 主题历史
     * @return 结果
     */
    public int insertTopicVersion(TopicVersion topicVersion);

    /**
     * 修改主题历史
     * 
     * @param topicVersion 主题历史
     * @return 结果
     */
    public int updateTopicVersion(TopicVersion topicVersion);

    /**
     * 删除主题历史
     * 
     * @param id 主题历史主键
     * @return 结果
     */
    public int deleteTopicVersionById(Long id);

    /**
     * 批量删除主题历史
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicVersionByIds(Long[] ids);

    /**
     * 查询主题编辑日志
     * @param topicId
     * @return
     */
    public List<TopicVersionVo> selectTopicVersionListByTopicId(Long topicId);

    public int updateTopicVersionByInfo(TopicVersion version);
}
