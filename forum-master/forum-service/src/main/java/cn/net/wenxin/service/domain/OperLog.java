package cn.net.wenxin.service.domain;

import java.util.Date;

import cn.net.wenxin.common.core.domain.BaseEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import cn.net.wenxin.common.annotation.Excel;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 操作日志记录对象 tb_oper_log
 * 
 * @author forum.wenxin.net.cn
 * @date 2023-08-23
 */
public class OperLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 用户ID */
    @Excel(name = "用户ID")
    private String userName;

    /** 模块类型 */
    @Excel(name = "模块类型")
    private Integer model;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除")
    private Integer businessType;

    /** 操作说明 */
    @Excel(name = "操作说明")
    private String title;

    /** 主机地址 */
    @Excel(name = "主机地址")
    private String operIp;

    /** 操作地点 */
    @Excel(name = "操作地点")
    private String operLocation;

    /** 请求参数 */
    @Excel(name = "请求参数")
    private String operParam;

    /** 返回参数 */
    @Excel(name = "返回参数")
    private String jsonResult;

    /** 操作状态（0正常 1异常） */
    private Integer status;

    /** 错误消息 */
    @Excel(name = "错误消息")
    private String errorMsg;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date operTime;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setUserName(String userName)
    {
        this.userName = userName;
    }

    public String getUserName()
    {
        return userName;
    }
    public void setModel(Integer model) 
    {
        this.model = model;
    }

    public Integer getModel() 
    {
        return model;
    }
    public void setBusinessType(Integer businessType) 
    {
        this.businessType = businessType;
    }

    public Integer getBusinessType() 
    {
        return businessType;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setOperIp(String operIp) 
    {
        this.operIp = operIp;
    }

    public String getOperIp() 
    {
        return operIp;
    }
    public void setOperLocation(String operLocation) 
    {
        this.operLocation = operLocation;
    }

    public String getOperLocation() 
    {
        return operLocation;
    }
    public void setOperParam(String operParam) 
    {
        this.operParam = operParam;
    }

    public String getOperParam() 
    {
        return operParam;
    }
    public void setJsonResult(String jsonResult) 
    {
        this.jsonResult = jsonResult;
    }

    public String getJsonResult() 
    {
        return jsonResult;
    }
    public void setStatus(Integer status) 
    {
        this.status = status;
    }

    public Integer getStatus() 
    {
        return status;
    }
    public void setErrorMsg(String errorMsg) 
    {
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() 
    {
        return errorMsg;
    }
    public void setOperTime(Date operTime) 
    {
        this.operTime = operTime;
    }

    public Date getOperTime() 
    {
        return operTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("userName", getUserName())
            .append("model", getModel())
            .append("businessType", getBusinessType())
            .append("title", getTitle())
            .append("operIp", getOperIp())
            .append("operLocation", getOperLocation())
            .append("operParam", getOperParam())
            .append("jsonResult", getJsonResult())
            .append("status", getStatus())
            .append("errorMsg", getErrorMsg())
            .append("operTime", getOperTime())
            .toString();
    }
}
