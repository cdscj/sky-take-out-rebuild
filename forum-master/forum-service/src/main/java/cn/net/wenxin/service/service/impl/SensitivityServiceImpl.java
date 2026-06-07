package cn.net.wenxin.service.service.impl;

import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.net.wenxin.common.core.redis.RedisCache;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.net.wenxin.service.mapper.SensitivityMapper;
import cn.net.wenxin.service.domain.Sensitivity;
import cn.net.wenxin.service.service.ISensitivityService;

/**
 * 敏感词Service业务层处理
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
@Service
public class SensitivityServiceImpl implements ISensitivityService
{
    private static final Logger log = LoggerFactory.getLogger(SensitivityServiceImpl.class);

    private static final String SENSITIVITY_CACHE_KEY = "forum:sensitivity:list";
    private static final long CACHE_TTL_SECONDS = 3600;

    @Autowired
    private SensitivityMapper sensitivityMapper;

    @Autowired
    private RedisCache redisCache;

    /**
     * 查询敏感词
     * 
     * @param id 敏感词主键
     * @return 敏感词
     */
    @Override
    public Sensitivity selectSensitivityById(Long id)
    {
        return sensitivityMapper.selectSensitivityById(id);
    }

    /**
     * 查询敏感词列表
     * 
     * @param sensitivity 敏感词
     * @return 敏感词
     */
    @Override
    public List<Sensitivity> selectSensitivityList(Sensitivity sensitivity)
    {
        return sensitivityMapper.selectSensitivityList(sensitivity);
    }

    /**
     * 新增敏感词
     * 
     * @param sensitivity 敏感词
     * @return 结果
     */
    @Override
    public int insertSensitivity(Sensitivity sensitivity)
    {
        sensitivity.setCreateTime(DateUtils.getNowDate());
        int result = sensitivityMapper.insertSensitivity(sensitivity);
        refreshSensitivityCache();
        return result;
    }

    /**
     * 修改敏感词
     * 
     * @param sensitivity 敏感词
     * @return 结果
     */
    @Override
    public int updateSensitivity(Sensitivity sensitivity)
    {
        sensitivity.setUpdateTime(DateUtils.getNowDate());
        int result = sensitivityMapper.updateSensitivity(sensitivity);
        refreshSensitivityCache();
        return result;
    }

    /**
     * 批量删除敏感词
     * 
     * @param ids 需要删除的敏感词主键
     * @return 结果
     */
    @Override
    public int deleteSensitivityByIds(Long[] ids)
    {
        int result = sensitivityMapper.deleteSensitivityByIds(ids);
        refreshSensitivityCache();
        return result;
    }

    /**
     * 删除敏感词信息
     *
     * @param id 敏感词主键
     * @return 结果
     */
    @Override
    public int deleteSensitivityById(Long id)
    {
        int result = sensitivityMapper.deleteSensitivityById(id);
        refreshSensitivityCache();
        return result;
    }

    @Override
    public String replaceSensitivity(String str) {
        if (StringUtils.isBlank(str)) {
            return str;
        }
        // Try Redis cache first
        List<Sensitivity> sensitivityList = redisCache.getCacheList(SENSITIVITY_CACHE_KEY);
        if (sensitivityList == null || sensitivityList.isEmpty()) {
            // Cache miss — query database
            sensitivityList = sensitivityMapper.selectSensitivityList(new Sensitivity());
            if (sensitivityList == null || sensitivityList.isEmpty()) {
                return str;
            }
            // Populate cache
            redisCache.deleteObject(SENSITIVITY_CACHE_KEY);
            redisCache.setCacheList(SENSITIVITY_CACHE_KEY, sensitivityList);
            redisCache.expire(SENSITIVITY_CACHE_KEY, CACHE_TTL_SECONDS, TimeUnit.SECONDS);
            log.debug("Sensitivity word list loaded from DB, {} entries cached", sensitivityList.size());
        }
        String[] searchArr = new String[sensitivityList.size()];
        String[] replacementArr = new String[sensitivityList.size()];
        int i = 0;
        for (Sensitivity sen : sensitivityList) {
            searchArr[i] = sen.getSearchs();
            replacementArr[i] = sen.getReplaces();
            i++;
        }
        return StringUtils.replaceEach(str, searchArr, replacementArr);
    }

    /**
     * Refresh sensitivity word cache — call after any CRUD operation on sensitivity words.
     */
    public void refreshSensitivityCache() {
        redisCache.deleteObject(SENSITIVITY_CACHE_KEY);
        log.info("Sensitivity word cache invalidated");
    }

}
