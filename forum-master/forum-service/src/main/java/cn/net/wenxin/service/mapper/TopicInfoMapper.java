package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.TopicInfo;
import cn.net.wenxin.service.domain.vo.TopicBaseVo;
import cn.net.wenxin.service.domain.vo.TopicDetailVo;
import org.apache.ibatis.annotations.Param;

/**
 * 主题Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface TopicInfoMapper 
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
     * @param orderIn 排序
     * @return 主题集合
     */
    public List<TopicDetailVo> selectTopicInfoList(@Param("orderIn") Integer orderIn,@Param("labelId") Long labelId,
                                                   @Param("username")String username,@Param("type")Integer type,
                                                   @Param("name")String name);

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
     * 删除主题
     * 
     * @param id 主题主键
     * @return 结果
     */
    public int deleteTopicInfoById(Long id);

    /**
     * 批量删除主题
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTopicInfoByIds(Long[] ids);

    /**
     * 主题搜索
     * @param name
     * @return
     */
    public List<TopicBaseVo> selectTopicBaseList(String name);
}
