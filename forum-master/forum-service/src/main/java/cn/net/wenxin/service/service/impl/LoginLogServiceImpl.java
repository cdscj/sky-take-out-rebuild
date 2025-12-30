package cn.net.wenxin.service.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.LoginLogMapper;
import cn.net.wenxin.service.domain.LoginLog;
import cn.net.wenxin.service.service.ILoginLogService;

/**
 * 用户访问记录Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-23
 */
@Service
public class LoginLogServiceImpl implements ILoginLogService 
{
    @Autowired
    private LoginLogMapper loginLogMapper;

    /**
     * 查询用户访问记录
     * 
     * @param id 用户访问记录主键
     * @return 用户访问记录
     */
    @Override
    public LoginLog selectLoginLogById(Long id)
    {
        return loginLogMapper.selectLoginLogById(id);
    }

    /**
     * 查询用户访问记录列表
     * 
     * @param loginLog 用户访问记录
     * @return 用户访问记录
     */
    @Override
    public List<LoginLog> selectLoginLogList(LoginLog loginLog)
    {
        return loginLogMapper.selectLoginLogList(loginLog);
    }

    /**
     * 新增用户访问记录
     * 
     * @param loginLog 用户访问记录
     * @return 结果
     */
    @Override
    public int insertLoginLog(LoginLog loginLog)
    {
        return loginLogMapper.insertLoginLog(loginLog);
    }

    /**
     * 修改用户访问记录
     * 
     * @param loginLog 用户访问记录
     * @return 结果
     */
    @Override
    public int updateLoginLog(LoginLog loginLog)
    {
        return loginLogMapper.updateLoginLog(loginLog);
    }

    /**
     * 批量删除用户访问记录
     * 
     * @param ids 需要删除的用户访问记录主键
     * @return 结果
     */
    @Override
    public int deleteLoginLogByIds(Long[] ids)
    {
        return loginLogMapper.deleteLoginLogByIds(ids);
    }

    /**
     * 删除用户访问记录信息
     * 
     * @param id 用户访问记录主键
     * @return 结果
     */
    @Override
    public int deleteLoginLogById(Long id)
    {
        return loginLogMapper.deleteLoginLogById(id);
    }

    @Override
    public void cleanLoginLog() {
        loginLogMapper.cleanLoginLog();
    }
}
