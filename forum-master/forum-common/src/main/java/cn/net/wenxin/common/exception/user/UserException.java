package cn.net.wenxin.common.exception.user;

import cn.net.wenxin.common.exception.base.BaseException;

/**
 * 用户信息异常类
 * 
 * @author forum.wenxin.net.cn
 */
public class UserException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public UserException(String code, Object[] args)
    {
        super("user", code, args, null);
    }
}
