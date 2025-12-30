package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.service.domain.User;
import cn.net.wenxin.service.domain.vo.TopicReplyVo;
import cn.net.wenxin.service.domain.vo.UserVo;
import cn.net.wenxin.service.mapper.UserMapper;
import cn.net.wenxin.service.service.IUserPointsService;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.TopicReplyMapper;
import cn.net.wenxin.service.domain.TopicReply;
import cn.net.wenxin.service.service.ITopicReplyService;

/**
 * 主题回复记录Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class TopicReplyServiceImpl implements ITopicReplyService 
{
    @Autowired
    private TopicReplyMapper topicReplyMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private IUserPointsService userPointsService;

    /**
     * 查询主题回复记录
     * 
     * @param id 主题回复记录主键
     * @return 主题回复记录
     */
    @Override
    public TopicReply selectTopicReplyById(Long id)
    {
        return topicReplyMapper.selectTopicReplyById(id);
    }

    /**
     * 查询主题回复记录列表
     * 
     * @param replyVo 主题回复记录
     * @return 主题回复记录
     */
    @Override
    public List<TopicReplyVo> selectTopicReplyList(TopicReplyVo replyVo)
    {
        return topicReplyMapper.selectTopicReplyVoList(replyVo);
    }

    /**
     * 新增主题回复记录
     * 
     * @param topicReply 主题回复记录
     * @return 结果
     */
    @Override
    public int insertTopicReply(TopicReply topicReply)
    {
        topicReply.setCreateTime(DateUtils.getNowDate());
        topicReply.setUpdateTime(DateUtils.getNowDate());
        int r =topicReplyMapper.insertTopicReply(topicReply);
        userPointsService.addUserPoints(topicReply.getCreateBy(),"2",topicReply.getId());
        return r;
    }

    /**
     * 修改主题回复记录
     * 
     * @param topicReply 主题回复记录
     * @return 结果
     */
    @Override
    public int updateTopicReply(TopicReply topicReply)
    {
        topicReply.setUpdateTime(DateUtils.getNowDate());
        return topicReplyMapper.updateTopicReply(topicReply);
    }

    /**
     * 批量删除主题回复记录
     * 
     * @param ids 需要删除的主题回复记录主键
     * @return 结果
     */
    @Override
    public int deleteTopicReplyByIds(Long[] ids)
    {
        return topicReplyMapper.deleteTopicReplyByIds(ids);
    }

    /**
     * 删除主题回复记录信息
     * 
     * @param id 主题回复记录主键
     * @return 结果
     */
    @Override
    public int deleteTopicReplyById(Long id)
    {
        return topicReplyMapper.deleteTopicReplyById(id);
    }

    @Override
    public List<TopicReply> selectTopicReplyMainList(Long topicId) {
        TopicReply reply = new TopicReply();
        reply.setTopicId(topicId);
        reply.setType(1);
        List<TopicReply> replyVos = topicReplyMapper.selectTopicReplyList(reply);
        if(replyVos != null && replyVos.size() >0){
            for (TopicReply replyVo : replyVos){
                User user = userMapper.selectUserByUserName(replyVo.getReplyerId());
                UserVo userVo = new UserVo();
                BeanUtils.copyBeanProp(userVo,user);
                replyVo.setUserVo(userVo);
            }
        }
        return replyVos;
    }

    @Override
    public List<TopicReply> getTopicReplyList(Long replyId) {
        TopicReply reply = new TopicReply();
        reply.setReplyMainId(replyId);
        List<TopicReply> replyVos = topicReplyMapper.selectTopicReplyList(reply);
        if(replyVos != null && replyVos.size() >0){
            for (TopicReply replyVo : replyVos){
                User user = userMapper.selectUserByUserName(replyVo.getReplyerId());
                UserVo userVo = new UserVo();
                BeanUtils.copyBeanProp(userVo,user);
                replyVo.setUserVo(userVo);
                if(replyVo.getReplyId() != null){
                    //针对评论的回复
                    TopicReply topicReply = topicReplyMapper.selectTopicReplyById(replyVo.getReplyId());
                    if(topicReply != null){
                        User ruser = userMapper.selectUserByUserName(topicReply.getReplyerId());
                        UserVo replyUser = new UserVo();
                        BeanUtils.copyBeanProp(replyUser,ruser);
                        replyVo.setReplyUser(replyUser);
                    }
                }

            }
        }
        return replyVos;
    }
}
