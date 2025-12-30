package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.Level;

/**
 * 级别Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface LevelMapper 
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
     * 删除级别
     * 
     * @param id 级别主键
     * @return 结果
     */
    public int deleteLevelById(Long id);

    /**
     * 批量删除级别
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLevelByIds(Long[] ids);

    /**
     * 查询
     * @param level
     * @return
     */
    public Level getCheckPointLevel(Level level);
}
