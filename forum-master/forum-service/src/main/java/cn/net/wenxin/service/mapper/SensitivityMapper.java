package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.Sensitivity;

/**
 * 敏感词Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface SensitivityMapper 
{
    /**
     * 查询敏感词
     * 
     * @param id 敏感词主键
     * @return 敏感词
     */
    public Sensitivity selectSensitivityById(Long id);

    /**
     * 查询敏感词列表
     * 
     * @param sensitivity 敏感词
     * @return 敏感词集合
     */
    public List<Sensitivity> selectSensitivityList(Sensitivity sensitivity);

    /**
     * 新增敏感词
     * 
     * @param sensitivity 敏感词
     * @return 结果
     */
    public int insertSensitivity(Sensitivity sensitivity);

    /**
     * 修改敏感词
     * 
     * @param sensitivity 敏感词
     * @return 结果
     */
    public int updateSensitivity(Sensitivity sensitivity);

    /**
     * 删除敏感词
     * 
     * @param id 敏感词主键
     * @return 结果
     */
    public int deleteSensitivityById(Long id);

    /**
     * 批量删除敏感词
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSensitivityByIds(Long[] ids);
}
