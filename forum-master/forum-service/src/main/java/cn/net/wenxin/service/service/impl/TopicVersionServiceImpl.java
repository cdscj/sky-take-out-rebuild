package cn.net.wenxin.service.service.impl;

import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.TopicVersionMapper;
import cn.net.wenxin.service.domain.TopicVersion;
import cn.net.wenxin.service.service.ITopicVersionService;

/**
 * 主题历史Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class TopicVersionServiceImpl implements ITopicVersionService 
{
    @Autowired
    private TopicVersionMapper topicVersionMapper;

    /**
     * 查询主题历史
     * 
     * @param id 主题历史主键
     * @return 主题历史
     */
    @Override
    public TopicVersion selectTopicVersionById(Long id)
    {
        return topicVersionMapper.selectTopicVersionById(id);
    }

    /**
     * 查询主题历史列表
     * 
     * @param topicVersion 主题历史
     * @return 主题历史
     */
    @Override
    public List<TopicVersion> selectTopicVersionList(TopicVersion topicVersion)
    {
        return topicVersionMapper.selectTopicVersionList(topicVersion);
    }

    /**
     * 新增主题历史
     * 
     * @param topicVersion 主题历史
     * @return 结果
     */
    @Override
    public int insertTopicVersion(TopicVersion topicVersion)
    {
        topicVersion.setCreateTime(DateUtils.getNowDate());
        return topicVersionMapper.insertTopicVersion(topicVersion);
    }

    /**
     * 修改主题历史
     * 
     * @param topicVersion 主题历史
     * @return 结果
     */
    @Override
    public int updateTopicVersion(TopicVersion topicVersion)
    {
        topicVersion.setUpdateTime(DateUtils.getNowDate());
        return topicVersionMapper.updateTopicVersion(topicVersion);
    }

    /**
     * 批量删除主题历史
     * 
     * @param ids 需要删除的主题历史主键
     * @return 结果
     */
    @Override
    public int deleteTopicVersionByIds(Long[] ids)
    {
        return topicVersionMapper.deleteTopicVersionByIds(ids);
    }

    /**
     * 删除主题历史信息
     * 
     * @param id 主题历史主键
     * @return 结果
     */
    @Override
    public int deleteTopicVersionById(Long id)
    {
        return topicVersionMapper.deleteTopicVersionById(id);
    }
}
