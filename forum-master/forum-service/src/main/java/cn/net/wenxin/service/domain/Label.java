package cn.net.wenxin.service.domain;

import cn.net.wenxin.common.annotation.Excel;
import cn.net.wenxin.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

/**
 * 标签对象 tb_label
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-22
 */
public class Label extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 标签id */
    private Long id;

    /** 父标签id */
    @Excel(name = "父标签id")
    private Long parentId;

    /** 标签名称 */
    @Excel(name = "标签名称")
    private String labelName;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Integer orderNum;

    /** 标签图片地址 */
    @Excel(name = "标签图片地址")
    private String labelImg;

    /** 标签说明 */
    @Excel(name = "标签说明")
    private String labelExplain;

    /** 部门状态（0正常 1停用） */
    @Excel(name = "部门状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    /** 子部门 */
    private List<Label> children = new ArrayList<Label>();

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setLabelName(String labelName) 
    {
        this.labelName = labelName;
    }

    @NotBlank(message = "标签名称不能为空")
    public String getLabelName()
    {
        return labelName;
    }
    public void setOrderNum(Integer orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Integer getOrderNum() 
    {
        return orderNum;
    }
    public void setLabelImg(String labelImg) 
    {
        this.labelImg = labelImg;
    }

    public String getLabelImg() 
    {
        return labelImg;
    }
    public void setLabelExplain(String labelExplain) 
    {
        this.labelExplain = labelExplain;
    }

    public String getLabelExplain() 
    {
        return labelExplain;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

    public List<Label> getChildren() {
        return children;
    }

    public void setChildren(List<Label> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("parentId", getParentId())
            .append("labelName", getLabelName())
            .append("orderNum", getOrderNum())
            .append("labelImg", getLabelImg())
            .append("labelExplain", getLabelExplain())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createBy", getCreateBy())
            .append("createTime", getCreateTime())
            .append("updateBy", getUpdateBy())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
