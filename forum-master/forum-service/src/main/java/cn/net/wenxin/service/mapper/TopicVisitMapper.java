package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.TopicVisit;

/**
 * 主题访问记录Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface TopicVisitMapper 
{
    /**
     * 查询主题访问记录
     * 
     * @param id 主题访问记录主键
     * @return 主题访问记录
     */
    public TopicVisit selectTopicVisitById(Long id);

    /**
     * 查询主题访问记录列表
     * 
     * @param topicVisit 主题访问记录
     * @return 主题访问记录集合
     */
    public List<TopicVisit> selectTopicVisitList(TopicVisit topicVisit);

    /**
     * 新增主题访问记录
     * 
     * @param topicVisit 主题访问记录
     * @return 结果
     */
    public int insertTopicVisit(TopicVisit topicVisit);

    /**
     * 修改主题访问记录
     * 
     * @param topicVisit 主题访问记录
     * @return 结果
     */
    public int updateTopicVisit(TopicVisit topicVisit);

    /**
     * 删除主题访问记录
     * 
     * @param id 主题访问记录主键
     * @return 结果
     */
    public int deleteTopicVisitById(Long id);

    /**
     * 批量删除主题访问记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicVisitByIds(Long[] ids);
}
