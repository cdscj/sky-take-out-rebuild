package cn.net.wenxin.service.service;

import java.util.List;
import cn.net.wenxin.service.domain.Level;

/**
 * 级别Service接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface ILevelService 
{
    /**
     * 查询级别
     * 
     * @param id 级别主键
     * @return 级别
     */
    public Level selectLevelById(Long id);

    /**
     * 查询级别列表
     * 
     * @param level 级别
     * @return 级别集合
     */
    public List<Level> selectLevelList(Level level);

    /**
     * 新增级别
     * 
     * @param level 级别
     * @return 结果
     */
    public int insertLevel(Level level);

    /**
     * 修改级别
     * 
     * @param level 级别
     * @return 结果
     */
    public int updateLevel(Level level);

    /**
     * 批量删除级别
     * 
     * @param ids 需要删除的级别主键集合
     * @return 结果
     */
    public int deleteLevelByIds(Long[] ids);

    /**
     * 删除级别信息
     * 
     * @param id 级别主键
     * @return 结果
     */
    public int deleteLevelById(Long id);

}
