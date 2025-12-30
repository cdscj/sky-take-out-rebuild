package cn.net.wenxin.service.service.impl;

import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.SystemSettingMapper;
import cn.net.wenxin.service.domain.SystemSetting;
import cn.net.wenxin.service.service.ISystemSettingService;

/**
 * 系统设置Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class SystemSettingServiceImpl implements ISystemSettingService 
{
    @Autowired
    private SystemSettingMapper systemSettingMapper;

    /**
     * 查询系统设置
     * 
     * @param id 系统设置主键
     * @return 系统设置
     */
    @Override
    public SystemSetting selectSystemSettingById(Long id)
    {
        return systemSettingMapper.selectSystemSettingById(id);
    }

    /**
     * 查询系统设置列表
     * 
     * @param systemSetting 系统设置
     * @return 系统设置
     */
    @Override
    public List<SystemSetting> selectSystemSettingList(SystemSetting systemSetting)
    {
        return systemSettingMapper.selectSystemSettingList(systemSetting);
    }

    /**
     * 新增系统设置
     * 
     * @param systemSetting 系统设置
     * @return 结果
     */
    @Override
    public int insertSystemSetting(SystemSetting systemSetting)
    {
        systemSetting.setCreateTime(DateUtils.getNowDate());
        return systemSettingMapper.insertSystemSetting(systemSetting);
    }

    /**
     * 修改系统设置
     * 
     * @param systemSetting 系统设置
     * @return 结果
     */
    @Override
    public int updateSystemSetting(SystemSetting systemSetting)
    {
        systemSetting.setUpdateTime(DateUtils.getNowDate());
        return systemSettingMapper.updateSystemSetting(systemSetting);
    }

    /**
     * 批量删除系统设置
     * 
     * @param ids 需要删除的系统设置主键
     * @return 结果
     */
    @Override
    public int deleteSystemSettingByIds(Long[] ids)
    {
        return systemSettingMapper.deleteSystemSettingByIds(ids);
    }

    /**
     * 删除系统设置信息
     * 
     * @param id 系统设置主键
     * @return 结果
     */
    @Override
    public int deleteSystemSettingById(Long id)
    {
        return systemSettingMapper.deleteSystemSettingById(id);
    }
}
