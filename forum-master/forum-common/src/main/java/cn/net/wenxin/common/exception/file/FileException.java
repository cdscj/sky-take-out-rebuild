package cn.net.wenxin.common.exception.file;

import cn.net.wenxin.common.exception.base.BaseException;

/**
 * 文件信息异常类
 * 
 * @author forum.wenxin.net.cn
 */
public class FileException extends BaseException
{
    private static final long serialVersionUID = 1L;

    public FileException(String code, Object[] args)
    {
        super("file", code, args, null);
    }

}
