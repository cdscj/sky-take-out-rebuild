package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.TopicReplyReport;
import cn.net.wenxin.service.domain.vo.TopicReplyReportVo;
import org.apache.ibatis.annotations.Param;

/**
 * 主题回复举报Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface TopicReplyReportMapper 
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
     * 删除主题回复举报
     * 
     * @param id 主题回复举报主键
     * @return 结果
     */
    public int deleteTopicReplyReportById(Long id);

    /**
     * 批量删除主题回复举报
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicReplyReportByIds(Long[] ids);

    /**
     * 查询主题回复举报人列表
     * @param reportVo
     * @return
     */
    public List<TopicReplyReportVo> selectReportUserList(TopicReplyReportVo reportVo);

    public int updateReportByHandle(TopicReplyReportVo reportVo);

    public TopicReplyReport getLatestInfo(@Param("topicId") Long topicId, @Param("replyId")Long replyId);
}
