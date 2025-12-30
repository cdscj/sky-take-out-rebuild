package cn.net.wenxin.common.enums;

/**
 * 用户状态
 * 
 * @author forum.wenxin.net.cn
 */
public enum PointsType
{
    PUBLISH("1", "tb_topic_info"),
    REPLY("2", "tb_topic_reply"),
    REPORT("3", "tb_topic_reply_report"),
    PRAISE("4", "tb_topic_reply_praise"),
    FOLLOW("5", "tb_user_follow");

    private final String code;
    private final String info;

    PointsType(String code, String info)
    {
        this.code = code;
        this.info = info;
    }

    // 普通方法
    public static String getInfo(String code) {
        if(code == null){
            return null;
        }
        for (PointsType c : PointsType.values()) {
            if (code.equals(c.getCode())) {
                return c.getInfo();
            }
        }
        return null;
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
