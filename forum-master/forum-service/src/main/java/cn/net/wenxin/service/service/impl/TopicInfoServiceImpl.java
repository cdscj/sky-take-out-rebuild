package cn.net.wenxin.service.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cn.net.wenxin.service.domain.*;
import cn.net.wenxin.service.domain.vo.*;
import cn.net.wenxin.service.mapper.*;
import cn.net.wenxin.service.service.ISensitivityService;
import cn.net.wenxin.service.service.IUserPointsService;
import cn.net.wenxin.common.exception.ServiceException;
import cn.net.wenxin.common.utils.*;
import cn.net.wenxin.common.utils.bean.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.service.ITopicInfoService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 主题Service业务层处理
 *
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class TopicInfoServiceImpl implements ITopicInfoService {
    @Autowired
    private TopicInfoMapper topicInfoMapper;
    @Autowired
    private TopicLabelMapper topicLabelMapper;
    @Autowired
    private TopicVersionMapper topicVersionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private TopicVisitMapper topicVisitMapper;
    @Autowired
    private UserFollowMapper userFollowMapper;
    @Autowired
    private ISensitivityService sensitivityService;
    @Autowired
    private TopicReplyPraiseMapper topicReplyPraiseMapper;
    @Autowired
    private IUserPointsService userPointsService;

    /**
     * 查询主题
     *
     * @param id 主题主键
     * @return 主题
     */
    @Override
    public TopicInfo selectTopicInfoById(Long id) {
        return topicInfoMapper.selectTopicInfoById(id);
    }

    /**
     * 查询主题列表
     *
     * @return 主题
     */
    @Override
    public List<TopicDetailVo> selectTopicInfoList(Integer orderIn,Long labelId,String username,Integer type,String name) {
        if(StringUtils.isBlank(username)){
            try {
                String loginUsername = SecurityUtils.getUsername();
                // 只有获取到用户名且type不为空时才使用用户名，否则保持username为null
                if(StringUtils.isNotBlank(loginUsername) && type != null){
                    username = loginUsername;
                }
            }catch (Exception e){
                // 匿名用户，保持username为null
            }
        }
        List<TopicDetailVo> detailVos = topicInfoMapper.selectTopicInfoList(orderIn,labelId,username,type,name);
        if (detailVos != null && !detailVos.isEmpty()) {
            // 收集所有话题 ID 和用户名
            List<Long> topicIds = detailVos.stream()
                    .map(TopicDetailVo::getId).distinct().collect(Collectors.toList());
            List<String> userNames = detailVos.stream()
                    .map(TopicDetailVo::getCreateBy).filter(n -> n != null).distinct()
                    .collect(Collectors.toList());
            // 批量查询标签
            List<TopicLabelVo> allLabels = topicLabelMapper.selectTopicLabelVoListByTopicIds(topicIds);
            Map<Long, List<TopicLabelVo>> labelMap = new HashMap<>();
            if (allLabels != null) {
                for (TopicLabelVo lv : allLabels) {
                    labelMap.computeIfAbsent(lv.getTopicId(), k -> new ArrayList<>()).add(lv);
                }
            }
            // 批量查询用户
            List<User> users = userMapper.selectUsersByUserNames(userNames);
            Map<String, User> userMap = new HashMap<>();
            if (users != null) {
                for (User u : users) {
                    userMap.put(u.getUserName(), u);
                }
            }
            // 组装
            for (TopicDetailVo detailVo : detailVos) {
                detailVo.setTitle(sensitivityService.replaceSensitivity(detailVo.getTitle()));
                detailVo.setTopicContent(sensitivityService.replaceSensitivity(detailVo.getTopicContent()));
                detailVo.setTopicLabelVoList(labelMap.getOrDefault(detailVo.getId(), new ArrayList<>()));
                User user = userMap.get(detailVo.getCreateBy());
                if (user != null) {
                    UserVo userVo = new UserVo();
                    BeanUtils.copyBeanProp(userVo, user);
                    detailVo.setCreateUser(userVo);
                }
                Date latestReplyDate = detailVo.getLatestReplyDate();
                if (latestReplyDate != null) {
                    detailVo.setLatestReplyDateName(DateHelper.getPastTime(latestReplyDate));
                }
            }
        }
        return detailVos;
    }

    /**
     * 查询主题列表
     *
     * @param topicInfo 主题
     * @return 主题
     */
    @Override
    public List<TopicDetailVo> selectManageTopicInfoList(TopicInfo topicInfo)
    {
        List<TopicDetailVo> detailVos = topicInfoMapper.selectManageTopicInfoList(topicInfo);
        if (detailVos != null && !detailVos.isEmpty()) {
            // 批量查询所有话题的标签
            List<Long> topicIds = detailVos.stream()
                    .map(TopicDetailVo::getId).distinct().collect(Collectors.toList());
            List<TopicLabelVo> allLabels = topicLabelMapper.selectTopicLabelVoListByTopicIds(topicIds);
            Map<Long, List<TopicLabelVo>> labelMap = new HashMap<>();
            if (allLabels != null) {
                for (TopicLabelVo lv : allLabels) {
                    labelMap.computeIfAbsent(lv.getTopicId(), k -> new ArrayList<>()).add(lv);
                }
            }
            for (TopicDetailVo detailVo : detailVos) {
                detailVo.setTopicLabelVoList(labelMap.getOrDefault(detailVo.getId(), new ArrayList<>()));
            }
        }
        return detailVos;
    }

    /**
     * 新增主题
     *
     * @param topicInfo 主题
     * @return 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertTopicInfo(TopicInfo topicInfo) {
        //提交审核
        topicInfo.setStatus("1");
        topicInfo.setCreateTime(DateUtils.getNowDate());
        topicInfo.setUpdateTime(DateUtils.getNowDate());
        topicInfoMapper.insertTopicInfo(topicInfo);

        Long topicId = topicInfo.getId();
        //增加标签关联
        List<Long> labelIds = topicInfo.getLabelIds();
        if(labelIds != null &&labelIds.size()>0){
            for(Long labelId : labelIds){
                TopicLabel tl = new TopicLabel();
                tl.setTopicId(topicId);
                tl.setLabelId(labelId);
                topicLabelMapper.insertTopicLabel(tl);
            }
        }
        //增加主题历史
        TopicVersion version = new TopicVersion();
        version.setTopicId(topicId);
        version.setType("1");
        version.setCreateBy(topicInfo.getCreateBy());
        version.setCreateTime(DateUtils.getNowDate());
        return topicVersionMapper.insertTopicVersion(version);
    }

    /**
     * 修改主题
     *
     * @param topicInfo 主题
     * @return 结果
     */
    @Override
    public int updateTopicInfo(TopicInfo topicInfo) {
        TopicInfo info = topicInfoMapper.selectTopicInfoById(topicInfo.getId());
        if(info == null){
            throw new ServiceException("查无记录");
        }
        topicInfo.setStatus("1");
        topicInfo.setUpdateTime(DateUtils.getNowDate());

        //增加主题历史
        TopicVersion version = new TopicVersion();
        version.setTopicId(topicInfo.getId());
        version.setType("2");
        version.setCreateBy(topicInfo.getCreateBy());
        version.setCreateTime(DateUtils.getNowDate());
        topicVersionMapper.insertTopicVersion(version);

        return topicInfoMapper.updateTopicInfo(topicInfo);
    }

    @Override
    public int checkTopicInfo(TopicInfo topicInfo) {
        TopicInfo info = topicInfoMapper.selectTopicInfoById(topicInfo.getId());
        if(info == null){
            throw new ServiceException("查无记录");
        }
        topicInfo.setUpdateTime(DateUtils.getNowDate());
        topicInfoMapper.updateTopicInfo(topicInfo);
        if("2".equals(topicInfo.getStatus())){
            userPointsService.addUserPoints(info.getCreateBy(),"1",topicInfo.getId());
        }
        //修改主题历史
        TopicVersion version = new TopicVersion();
        version.setTopicId(topicInfo.getId());
        version.setAuditStatus(topicInfo.getStatus());
        version.setAuditUser(SecurityUtils.getUsername());
        version.setAuditTime(DateUtils.getNowDate());
        version.setUpdateBy(SecurityUtils.getUsername());
        version.setUpdateTime(DateUtils.getNowDate());
        return topicVersionMapper.updateTopicVersionByInfo(version);
    }

    /**
     * 批量删除主题
     *
     * @param ids 需要删除的主题主键
     * @return 结果
     */
    @Override
    public int deleteTopicInfoByIds(Long[] ids) {
        return topicInfoMapper.deleteTopicInfoByIds(ids);
    }

    /**
     * 删除主题信息
     *
     * @param id 主题主键
     * @return 结果
     */
    @Override
    public int deleteTopicInfoById(Long id) {
        return topicInfoMapper.deleteTopicInfoById(id);
    }

    @Override
    public TopicDetailVo selectTopicInfoDetail(Long topicId) {
        String userName = null;
        try {
            userName =  SecurityUtils.getUsername();
        }catch (Exception e){
        }
        TopicInfo info = topicInfoMapper.selectTopicInfoById(topicId);
        if(info != null){
            visitTopic(topicId);
            TopicDetailVo detailVo = new TopicDetailVo();
            BeanUtils.copyBeanProp(detailVo,info);
            detailVo.setTitle(sensitivityService.replaceSensitivity(detailVo.getTitle()));
            detailVo.setTopicContent(sensitivityService.replaceSensitivity(detailVo.getTopicContent()));
            //组装标签等扩展信息
            List<TopicLabelVo> labelVos = topicLabelMapper.selectTopicLabelVoList(detailVo.getId());
            if(labelVos != null && labelVos.size()>0){
                detailVo.setTopicLabelVoList(labelVos);
            }

            User user = userMapper.selectUserByUserName(detailVo.getCreateBy());
            if(user != null){
                UserVo userVo = new UserVo();
                BeanUtils.copyBeanProp(userVo,user);
                detailVo.setCreateUser(userVo);
            }
            Date  latestReplyDate = detailVo.getLatestReplyDate();
            if(latestReplyDate != null){
                detailVo.setLatestReplyDateName(DateHelper.getPastTime(latestReplyDate));
            }
            List<TopicVersionVo> versionVos = topicVersionMapper.selectTopicVersionListByTopicId(detailVo.getId());
            if(versionVos != null){
                detailVo.setVersionVos(versionVos);
            }
            if(StringUtils.isNotBlank(userName)){
               int follow =  userFollowMapper.selectFollowByTopicIdAndUser(detailVo.getId(),userName);
                detailVo.setFollow(follow);
                int praise = topicReplyPraiseMapper.selectPraiseByTopicIdAndUser(detailVo.getId(),userName);
                if(praise >0){
                    detailVo.setPraise(1);
                }else{
                    detailVo.setPraise(0);
                }
            }
            return detailVo;
        }
        return null;
    }

    @Override
    public List<TopicBaseVo> selectTopicBaseList(String name) {
        // todo
        return topicInfoMapper.selectTopicBaseList(name);
    }

    @Override
    public int cancelTopicInfo(Long id) {
        String userName = null;
        try {
            userName =  SecurityUtils.getUsername();
        }catch (Exception e){
        }
        TopicInfo info = topicInfoMapper.selectTopicInfoById(id);
        if(info != null){
            info.setStatus("4");
            info.setUpdateTime(new Date());
            info.setUpdateBy(userName);
            return topicInfoMapper.updateTopicInfo(info);
        }
        return 0;
    }

    /**
     * 记录主题访问
     * @param topicId
     */
    private void visitTopic(Long topicId){
        String userName = null;
        try {
            userName =  SecurityUtils.getUsername();
            if(StringUtils.isBlank(userName)){
                userName = "-1";
            }
        }catch (Exception e){
            userName = "-1";
        }
        TopicVisit visit = new TopicVisit();
        visit.setTopicId(topicId);
        visit.setUserId(userName);
        visit.setCreateTime(DateUtils.getNowDate());
        visit.setCreateBy(userName);
        topicVisitMapper.insertTopicVisit(visit);
    }
}
