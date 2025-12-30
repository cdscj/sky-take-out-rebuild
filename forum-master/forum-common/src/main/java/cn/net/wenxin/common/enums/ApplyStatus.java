package cn.net.wenxin.common.enums;

/**
 * 审批状态
 * 
 */
public enum ApplyStatus
{
    FIRST("1", "初审"), FIRSTFAILED("2", "初审不通过"),
    REVIEW("3", "复审"),COMPLETE("4", "已完成"),DRAFT("5", "草稿"),;

    private final String code;
    private final String info;

    ApplyStatus(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    public String getCode()
    {
        return code;
    }

    public String getInfo()
    {
        return info;
    }
}
