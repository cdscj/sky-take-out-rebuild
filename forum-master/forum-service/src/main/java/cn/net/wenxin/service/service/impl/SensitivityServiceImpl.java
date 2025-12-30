package cn.net.wenxin.service.service.impl;

import java.util.ArrayList;
import java.util.List;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.StringUtils;
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
    @Autowired
    private SensitivityMapper sensitivityMapper;

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
        return sensitivityMapper.insertSensitivity(sensitivity);
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
        return sensitivityMapper.updateSensitivity(sensitivity);
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
        return sensitivityMapper.deleteSensitivityByIds(ids);
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
        return sensitivityMapper.deleteSensitivityById(id);
    }

    @Override
    public String replaceSensitivity(String str) {
        StringBuilder sb = new StringBuilder();
        if (StringUtils.isBlank(str)) {
            return str;
        }
        List<Sensitivity> sensitivityList = new ArrayList<>();
        if (sensitivityList == null) {
            sensitivityList = sensitivityMapper.selectSensitivityList(new Sensitivity());
            if(sensitivityList == null || sensitivityList.size()<=0){
                return str;
            }
        }
        String[] searchArr = new String[sensitivityList.size()];
        String[] replacementArr = new String[sensitivityList.size()];
        int i = 0;
        for (Sensitivity sen : sensitivityList) {
            searchArr[i] = sen.getSearchs();
            replacementArr[i] = sen.getReplaces();
            i++;
        }
        sb.append(StringUtils.replaceEach(str, searchArr,
                replacementArr));
        return sb.toString();
    }

}
