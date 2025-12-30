package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.ip.AddressUtils;
import cn.net.wenxin.common.utils.ip.IpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.OperLogMapper;
import cn.net.wenxin.service.domain.OperLog;
import cn.net.wenxin.service.service.IOperLogService;

/**
 * 操作日志记录Service业务层处理
 *
 * @author forum.wenxin.net.cn
 * @date 2023-08-23
 */
@Service
public class OperLogServiceImpl implements IOperLogService {
    @Autowired
    private OperLogMapper operLogMapper;

    /**
     * 查询操作日志记录
     *
     * @param id 操作日志记录主键
     * @return 操作日志记录
     */
    @Override
    public OperLog selectOperLogById(Long id) {
        return operLogMapper.selectOperLogById(id);
    }

    /**
     * 查询操作日志记录列表
     *
     * @param operLog 操作日志记录
     * @return 操作日志记录
     */
    @Override
    public List<OperLog> selectOperLogList(OperLog operLog) {
        return operLogMapper.selectOperLogList(operLog);
    }

    /**
     * 新增操作日志记录
     *
     * @param operLog 操作日志记录
     * @return 结果
     */
    @Override
    public int insertOperLog(OperLog operLog) {
        return operLogMapper.insertOperLog(operLog);
    }

    @Override
    public int insertUserOperLog(String username, int type, String explain) {
        OperLog operLog = new OperLog();
        operLog.setUserName(username);
        operLog.setBusinessType(type);
        operLog.setTitle(explain);
        // 远程查询操作地点
        String ip = IpUtils.getIpAddr();
        String address = AddressUtils.getRealAddressByIP(ip);
        operLog.setOperLocation(address);
        operLog.setOperIp(ip);
        operLog.setOperTime(DateUtils.getNowDate());
        return operLogMapper.insertOperLog(operLog);
    }

    /**
     * 修改操作日志记录
     *
     * @param operLog 操作日志记录
     * @return 结果
     */
    @Override
    public int updateOperLog(OperLog operLog) {
        return operLogMapper.updateOperLog(operLog);
    }

    /**
     * 批量删除操作日志记录
     *
     * @param ids 需要删除的操作日志记录主键
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] ids) {
        return operLogMapper.deleteOperLogByIds(ids);
    }

    /**
     * 删除操作日志记录信息
     *
     * @param id 操作日志记录主键
     * @return 结果
     */
    @Override
    public int deleteOperLogById(Long id) {
        return operLogMapper.deleteOperLogById(id);
    }

    @Override
    public void cleanOperLog() {
        operLogMapper.cleanOperLog();
    }
}
