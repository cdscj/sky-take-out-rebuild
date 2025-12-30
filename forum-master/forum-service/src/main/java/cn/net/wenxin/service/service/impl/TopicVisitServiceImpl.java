package cn.net.wenxin.service.service.impl;

import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.TopicVisitMapper;
import cn.net.wenxin.service.domain.TopicVisit;
import cn.net.wenxin.service.service.ITopicVisitService;

/**
 * 主题访问记录Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class TopicVisitServiceImpl implements ITopicVisitService 
{
    @Autowired
    private TopicVisitMapper topicVisitMapper;

    /**
     * 查询主题访问记录
     * 
     * @param id 主题访问记录主键
     * @return 主题访问记录
     */
    @Override
    public TopicVisit selectTopicVisitById(Long id)
    {
        return topicVisitMapper.selectTopicVisitById(id);
    }

    /**
     * 查询主题访问记录列表
     * 
     * @param topicVisit 主题访问记录
     * @return 主题访问记录
     */
    @Override
    public List<TopicVisit> selectTopicVisitList(TopicVisit topicVisit)
    {
        return topicVisitMapper.selectTopicVisitList(topicVisit);
    }

    /**
     * 新增主题访问记录
     * 
     * @param topicVisit 主题访问记录
     * @return 结果
     */
    @Override
    public int insertTopicVisit(TopicVisit topicVisit)
    {
        topicVisit.setCreateTime(DateUtils.getNowDate());
        return topicVisitMapper.insertTopicVisit(topicVisit);
    }

    /**
     * 修改主题访问记录
     * 
     * @param topicVisit 主题访问记录
     * @return 结果
     */
    @Override
    public int updateTopicVisit(TopicVisit topicVisit)
    {
        topicVisit.setUpdateTime(DateUtils.getNowDate());
        return topicVisitMapper.updateTopicVisit(topicVisit);
    }

    /**
     * 批量删除主题访问记录
     * 
     * @param ids 需要删除的主题访问记录主键
     * @return 结果
     */
    @Override
    public int deleteTopicVisitByIds(Long[] ids)
    {
        return topicVisitMapper.deleteTopicVisitByIds(ids);
    }

    /**
     * 删除主题访问记录信息
     * 
     * @param id 主题访问记录主键
     * @return 结果
     */
    @Override
    public int deleteTopicVisitById(Long id)
    {
        return topicVisitMapper.deleteTopicVisitById(id);
    }
}
