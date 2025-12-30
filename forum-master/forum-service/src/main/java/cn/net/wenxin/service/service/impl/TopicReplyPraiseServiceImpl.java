package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.service.service.IUserPointsService;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.TopicReplyPraiseMapper;
import cn.net.wenxin.service.domain.TopicReplyPraise;
import cn.net.wenxin.service.service.ITopicReplyPraiseService;

/**
 * 主题回复点赞Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class TopicReplyPraiseServiceImpl implements ITopicReplyPraiseService 
{
    @Autowired
    private TopicReplyPraiseMapper topicReplyPraiseMapper;
    @Autowired
    private IUserPointsService userPointsService;

    /**
     * 查询主题回复点赞
     * 
     * @param id 主题回复点赞主键
     * @return 主题回复点赞
     */
    @Override
    public TopicReplyPraise selectTopicReplyPraiseById(Long id)
    {
        return topicReplyPraiseMapper.selectTopicReplyPraiseById(id);
    }

    /**
     * 查询主题回复点赞列表
     * 
     * @param topicReplyPraise 主题回复点赞
     * @return 主题回复点赞
     */
    @Override
    public List<TopicReplyPraise> selectTopicReplyPraiseList(TopicReplyPraise topicReplyPraise)
    {
        return topicReplyPraiseMapper.selectTopicReplyPraiseList(topicReplyPraise);
    }

    /**
     * 新增主题回复点赞
     * 
     * @param topicReplyPraise 主题回复点赞
     * @return 结果
     */
    @Override
    public int insertTopicReplyPraise(TopicReplyPraise topicReplyPraise)
    {
        topicReplyPraise.setCreateTime(DateUtils.getNowDate());
        topicReplyPraise.setUpdateTime(DateUtils.getNowDate());
        int r = topicReplyPraiseMapper.insertTopicReplyPraise(topicReplyPraise);
        userPointsService.addUserPoints(topicReplyPraise.getCreateBy(),"4",topicReplyPraise.getId());
        return r;
    }

    /**
     * 修改主题回复点赞
     * 
     * @param topicReplyPraise 主题回复点赞
     * @return 结果
     */
    @Override
    public int updateTopicReplyPraise(TopicReplyPraise topicReplyPraise)
    {
        topicReplyPraise.setUpdateTime(DateUtils.getNowDate());
        return topicReplyPraiseMapper.updateTopicReplyPraise(topicReplyPraise);
    }

    /**
     * 批量删除主题回复点赞
     * 
     * @param ids 需要删除的主题回复点赞主键
     * @return 结果
     */
    @Override
    public int deleteTopicReplyPraiseByIds(Long[] ids)
    {
        return topicReplyPraiseMapper.deleteTopicReplyPraiseByIds(ids);
    }

    /**
     * 删除主题回复点赞信息
     * 
     * @param id 主题回复点赞主键
     * @return 结果
     */
    @Override
    public int deleteTopicReplyPraiseById(Long id)
    {
        return topicReplyPraiseMapper.deleteTopicReplyPraiseById(id);
    }

    @Override
    public int unpraise(TopicReplyPraise praise) {
        return topicReplyPraiseMapper.unpraise(praise);
    }
}
