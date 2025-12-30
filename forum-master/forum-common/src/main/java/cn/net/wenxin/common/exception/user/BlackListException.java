package cn.net.wenxin.common.exception.user;

/**
 * 黑名单IP异常类
 * 
 * @author forum.wenxin.net.cn
 */
public class BlackListException extends UserException
{
    private static final long serialVersionUID = 1L;

    public BlackListException()
    {
        super("login.blocked", null);
    }
}
