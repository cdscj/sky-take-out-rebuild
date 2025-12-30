package cn.net.wenxin.service.service.impl;

import java.util.List;

import cn.net.wenxin.common.exception.ServiceException;
import cn.net.wenxin.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.LevelMapper;
import cn.net.wenxin.service.domain.Level;
import cn.net.wenxin.service.service.ILevelService;

/**
 * 级别Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class LevelServiceImpl implements ILevelService 
{
    @Autowired
    private LevelMapper levelMapper;

    /**
     * 查询级别
     * 
     * @param id 级别主键
     * @return 级别
     */
    @Override
    public Level selectLevelById(Long id)
    {
        return levelMapper.selectLevelById(id);
    }

    /**
     * 查询级别列表
     * 
     * @param level 级别
     * @return 级别
     */
    @Override
    public List<Level> selectLevelList(Level level)
    {
        return levelMapper.selectLevelList(level);
    }

    /**
     * 新增级别
     * 
     * @param level 级别
     * @return 结果
     */
    @Override
    public int insertLevel(Level level)
    {
        if(!checkPoint(level)){
            throw new ServiceException("已有当前积分规则");
        }
        level.setCreateTime(DateUtils.getNowDate());
        return levelMapper.insertLevel(level);
    }

    /**
     * 修改级别
     * 
     * @param level 级别
     * @return 结果
     */
    @Override
    public int updateLevel(Level level)
    {
        if(!checkPoint(level)){
            throw new ServiceException("已有当前积分规则");
        }
        level.setUpdateTime(DateUtils.getNowDate());
        return levelMapper.updateLevel(level);
    }

    /**
     * 批量删除级别
     * 
     * @param ids 需要删除的级别主键
     * @return 结果
     */
    @Override
    public int deleteLevelByIds(Long[] ids)
    {
        return levelMapper.deleteLevelByIds(ids);
    }

    /**
     * 删除级别信息
     * 
     * @param id 级别主键
     * @return 结果
     */
    @Override
    public int deleteLevelById(Long id)
    {
        return levelMapper.deleteLevelById(id);
    }

    /**
     * 检查积分范围
     * @return
     */
    private boolean checkPoint(Level level){
        if(level == null || level.getPointsMin() == null || level.getPointsMax() == null
                || level.getPointsMin().longValue() >= level.getPointsMax().longValue()){
            return false;
        }
        Level ol = levelMapper.getCheckPointLevel(level);
        if(ol == null){
            return true;
        }else{
            if(ol.getId().equals(level.getId())){
                return true;
            }
        }
        return false;
    }
}
