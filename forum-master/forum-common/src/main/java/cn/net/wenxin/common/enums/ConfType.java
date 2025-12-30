package cn.net.wenxin.common.enums;

/**
 * 审批状态
 * 
 */
public enum ConfType
{
    FIRST("FIRST", "初审"), FIRSTFAILED("RECHECK", "复审"),;

    private final String code;
    private final String name;

    ConfType(String code, String name)
    {
        this.code = code;
        this.name = name;
    }

    public static String getName(String code) {
        ConfType[] confTypeEnums = values();
        for (ConfType confType : confTypeEnums) {
            if (confType.code.equals(code)) {
                return confType.name;
            }
        }
        return null;
    }

    public static String getCode(String name) {
        ConfType[] confTypeEnums = values();
        for (ConfType confType : confTypeEnums) {
            if (confType.name.equals(name)) {
                return confType.code;
            }
        }
        return null;
    }


    public String getCode()
    {
        return code;
    }

    public String getName()
    {
        return name;
    }
}
