package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 敏感词对象 tb_sensitivity
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class Sensitivity extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主题id */
    private Long id;

    /** 敏感词 */
    @Excel(name = "敏感词")
    private String searchs;

    /** 替换词 */
    @Excel(name = "替换词")
    private String replaces;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setSearchs(String searchs) 
    {
        this.searchs = searchs;
    }

    public String getSearchs() 
    {
        return searchs;
    }
    public void setReplaces(String replaces) 
    {
        this.replaces = replaces;
    }

    public String getReplaces() 
    {
        return replaces;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("searchs", getSearchs())
            .append("replaces", getReplaces())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
