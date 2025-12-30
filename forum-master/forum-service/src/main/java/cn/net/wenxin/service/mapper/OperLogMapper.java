package cn.net.wenxin.service.mapper;

import java.util.List;
import cn.net.wenxin.service.domain.OperLog;

/**
 * 操作日志记录Mapper接口
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-23
 */
public interface OperLogMapper 
{
    /**
     * 查询操作日志记录
     * 
     * @param id 操作日志记录主键
     * @return 操作日志记录
     */
    public OperLog selectOperLogById(Long id);

    /**
     * 查询操作日志记录列表
     * 
     * @param operLog 操作日志记录
     * @return 操作日志记录集合
     */
    public List<OperLog> selectOperLogList(OperLog operLog);

    /**
     * 新增操作日志记录
     * 
     * @param operLog 操作日志记录
     * @return 结果
     */
    public int insertOperLog(OperLog operLog);

    /**
     * 修改操作日志记录
     * 
     * @param operLog 操作日志记录
     * @return 结果
     */
    public int updateOperLog(OperLog operLog);

    /**
     * 删除操作日志记录
     * 
     * @param id 操作日志记录主键
     * @return 结果
     */
    public int deleteOperLogById(Long id);

    /**
     * 批量删除操作日志记录
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteOperLogByIds(Long[] ids);

    /**
     * 清空日志
     */
    public void cleanOperLog();
}
