package cn.net.wenxin.service.service;

import java.util.List;

import cn.net.wenxin.service.domain.TopicReplyReport;
import cn.net.wenxin.service.domain.vo.TopicReplyReportVo;

/**
 * 主题回复举报Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface ITopicReplyReportService 
{
    /**
     * 查询主题回复举报
     * 
     * @param id 主题回复举报主键
     * @return 主题回复举报
     */
    public TopicReplyReport selectTopicReplyReportById(Long id);

    /**
     * 查询主题回复举报列表
     * 
     * @param reportVo 主题回复举报
     * @return 主题回复举报集合
     */
    public List<TopicReplyReportVo> selectTopicReplyReportList(TopicReplyReportVo reportVo);


    /**
     * 查询主题回复举报人列表
     * @param reportVo
     * @return
     */
    public List<TopicReplyReportVo> selectReportUserList(TopicReplyReportVo reportVo);

    /**
     * 新增主题回复举报
     * 
     * @param topicReplyReport 主题回复举报
     * @return 结果
     */
    public int insertTopicReplyReport(TopicReplyReport topicReplyReport);

    /**
     * 修改主题回复举报
     * 
     * @param topicReplyReport 主题回复举报
     * @return 结果
     */
    public int updateTopicReplyReport(TopicReplyReport topicReplyReport);

    /**
     * 批量删除主题回复举报
     * 
     * @param ids 需要删除的主题回复举报主键集合
     * @return 结果
     */
    public int deleteTopicReplyReportByIds(Long[] ids);

    /**
     * 删除主题回复举报信息
     * 
     * @param id 主题回复举报主键
     * @return 结果
     */
    public int deleteTopicReplyReportById(Long id);

    public int handleReport(TopicReplyReportVo reportVo);
}
