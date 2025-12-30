package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.LoginLog;

/**
 * 用户访问记录Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-23
 */
public interface LoginLogMapper 
{
    /**
     * 查询用户访问记录
     * 
     * @param id 用户访问记录主键
     * @return 用户访问记录
     */
    public LoginLog selectLoginLogById(Long id);

    /**
     * 查询用户访问记录列表
     * 
     * @param loginLog 用户访问记录
     * @return 用户访问记录集合
     */
    public List<LoginLog> selectLoginLogList(LoginLog loginLog);

    /**
     * 新增用户访问记录
     * 
     * @param loginLog 用户访问记录
     * @return 结果
     */
    public int insertLoginLog(LoginLog loginLog);

    /**
     * 修改用户访问记录
     * 
     * @param loginLog 用户访问记录
     * @return 结果
     */
    public int updateLoginLog(LoginLog loginLog);

    /**
     * 删除用户访问记录
     * 
     * @param id 用户访问记录主键
     * @return 结果
     */
    public int deleteLoginLogById(Long id);

    /**
     * 批量删除用户访问记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteLoginLogByIds(Long[] ids);

    /**
     * 清空日志
     */
    public void cleanLoginLog();
}
