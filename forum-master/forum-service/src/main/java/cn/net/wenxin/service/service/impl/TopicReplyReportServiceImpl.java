package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.service.domain.TopicReplyReport;
import cn.net.wenxin.service.domain.vo.TopicReplyReportVo;
import cn.net.wenxin.service.mapper.TopicInfoMapper;
import cn.net.wenxin.service.mapper.TopicReplyMapper;
import cn.net.wenxin.service.service.IUserPointsService;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.TopicReplyReportMapper;
import cn.net.wenxin.service.service.ITopicReplyReportService;

/**
 * 主题回复举报Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class TopicReplyReportServiceImpl implements ITopicReplyReportService 
{
    @Autowired
    private TopicReplyReportMapper topicReplyReportMapper;
    @Autowired
    private IUserPointsService userPointsService;
    @Autowired
    private TopicInfoMapper topicInfoMapper;
    @Autowired
    private TopicReplyMapper topicReplyMapper;

    /**
     * 查询主题回复举报
     * 
     * @param id 主题回复举报主键
     * @return 主题回复举报
     */
    @Override
    public TopicReplyReport selectTopicReplyReportById(Long id)
    {
        return topicReplyReportMapper.selectTopicReplyReportById(id);
    }

    /**
     * 查询主题回复举报列表
     * 
     * @param reportVo 主题回复举报
     * @return 主题回复举报
     */
    @Override
    public List<TopicReplyReportVo> selectTopicReplyReportList(TopicReplyReportVo reportVo)
    {
        List<TopicReplyReportVo> resultList = topicReplyReportMapper.selectTopicReplyReportList(reportVo);
        if(resultList != null && resultList.size()>0){
            for(TopicReplyReportVo report : resultList){
                TopicReplyReport replyReport = topicReplyReportMapper.getLatestInfo(report.getTopicId(),report.getReplyId());
                if(replyReport != null){
                    report.setResultStatus(replyReport.getResultStatus());
                    report.setReportResult(replyReport.getReportResult());
                }
            }
        }
        return resultList;
    }

    /**
     * 查询主题回复举报人列表
     * @param reportVo
     * @return
     */
    @Override
    public List<TopicReplyReportVo> selectReportUserList(TopicReplyReportVo reportVo)
    {
        return topicReplyReportMapper.selectReportUserList(reportVo);
    }

    /**
     * 新增主题回复举报
     * 
     * @param topicReplyReport 主题回复举报
     * @return 结果
     */
    @Override
    public int insertTopicReplyReport(TopicReplyReport topicReplyReport)
    {
        topicReplyReport.setCreateTime(DateUtils.getNowDate());
        topicReplyReport.setUpdateTime(DateUtils.getNowDate());
        int r = topicReplyReportMapper.insertTopicReplyReport(topicReplyReport);
        userPointsService.addUserPoints(topicReplyReport.getCreateBy(),"3",topicReplyReport.getId());
        return r;
    }

    /**
     * 修改主题回复举报
     * 
     * @param topicReplyReport 主题回复举报
     * @return 结果
     */
    @Override
    public int updateTopicReplyReport(TopicReplyReport topicReplyReport)
    {
        topicReplyReport.setUpdateTime(DateUtils.getNowDate());
        return topicReplyReportMapper.updateTopicReplyReport(topicReplyReport);
    }

    /**
     * 批量删除主题回复举报
     * 
     * @param ids 需要删除的主题回复举报主键
     * @return 结果
     */
    @Override
    public int deleteTopicReplyReportByIds(Long[] ids)
    {
        return topicReplyReportMapper.deleteTopicReplyReportByIds(ids);
    }

    /**
     * 删除主题回复举报信息
     * 
     * @param id 主题回复举报主键
     * @return 结果
     */
    @Override
    public int deleteTopicReplyReportById(Long id)
    {
        return topicReplyReportMapper.deleteTopicReplyReportById(id);
    }

    @Override
    public int handleReport(TopicReplyReportVo reportVo) {
        if(reportVo.getResultStatus() != null && reportVo.getResultStatus()==1){
            //驳回
            topicReplyReportMapper.updateReportByHandle(reportVo);
        }else if(reportVo.getResultStatus() != null && reportVo.getResultStatus()==2){
            //删除对应举报
            if(reportVo.getType() == 1){
                topicInfoMapper.deleteTopicInfoById(reportVo.getTopicId());
            }else if(reportVo.getType() == 2){
                topicReplyMapper.deleteTopicReplyById(reportVo.getReplyId());
            }
            topicReplyReportMapper.updateReportByHandle(reportVo);
        }
        return 1;
    }
}
