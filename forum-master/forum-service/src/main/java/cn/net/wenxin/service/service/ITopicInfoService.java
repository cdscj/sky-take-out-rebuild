package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.TopicInfo;
import cn.net.wenxin.service.domain.vo.TopicBaseVo;
import cn.net.wenxin.service.domain.vo.TopicDetailVo;

/**
 * 主题Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface ITopicInfoService 
{
    /**
     * 查询主题
     * 
     * @param id 主题主键
     * @return 主题
     */
    public TopicInfo selectTopicInfoById(Long id);

    /**
     * 查询主题列表
     * 
     * @return 主题集合
     */
    public List<TopicDetailVo> selectTopicInfoList(Integer orderIn,Long labelId,String username,Integer type,String name);

    public List<TopicDetailVo> selectManageTopicInfoList(TopicInfo topicInfo);

    /**
     * 新增主题
     * 
     * @param topicInfo 主题
     * @return 结果
     */
    public int insertTopicInfo(TopicInfo topicInfo);

    /**
     * 修改主题
     * 
     * @param topicInfo 主题
     * @return 结果
     */
    public int updateTopicInfo(TopicInfo topicInfo);

    /**
     * 批量删除主题
     * 
     * @param ids 需要删除的主题主键集合
     * @return 结果
     */
    public int deleteTopicInfoByIds(Long[] ids);

    /**
     * 删除主题信息
     * 
     * @param id 主题主键
     * @return 结果
     */
    public int deleteTopicInfoById(Long id);

    /**
     * 主题详细信息
     * @param topicId
     * @return
     */
    public TopicDetailVo selectTopicInfoDetail(Long topicId);

    /**
     * 主题搜索
     * @param name
     * @return
     */
    public List<TopicBaseVo> selectTopicBaseList(String name);

    /**
     * 撤销主题
     * @param id
     * @return
     */
    public int cancelTopicInfo(Long id);

    /**
     * 审核主题
     * @param topicInfo
     * @return
     */
    public int checkTopicInfo(TopicInfo topicInfo);
}
