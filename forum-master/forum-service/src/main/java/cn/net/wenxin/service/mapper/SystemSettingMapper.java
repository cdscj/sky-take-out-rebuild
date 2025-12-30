package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.SystemSetting;

/**
 * 系统设置Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public interface SystemSettingMapper 
{
    /**
     * 查询系统设置
     * 
     * @param id 系统设置主键
     * @return 系统设置
     */
    public SystemSetting selectSystemSettingById(Long id);

    /**
     * 查询系统设置列表
     * 
     * @param systemSetting 系统设置
     * @return 系统设置集合
     */
    public List<SystemSetting> selectSystemSettingList(SystemSetting systemSetting);

    /**
     * 新增系统设置
     * 
     * @param systemSetting 系统设置
     * @return 结果
     */
    public int insertSystemSetting(SystemSetting systemSetting);

    /**
     * 修改系统设置
     * 
     * @param systemSetting 系统设置
     * @return 结果
     */
    public int updateSystemSetting(SystemSetting systemSetting);

    /**
     * 删除系统设置
     * 
     * @param id 系统设置主键
     * @return 结果
     */
    public int deleteSystemSettingById(Long id);

    /**
     * 批量删除系统设置
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteSystemSettingByIds(Long[] ids);
}
