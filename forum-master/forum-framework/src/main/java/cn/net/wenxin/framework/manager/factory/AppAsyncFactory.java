package cn.net.wenxin.framework.manager.factory;

import cn.net.wenxin.service.domain.LoginLog;
import cn.net.wenxin.service.domain.OperLog;
import cn.net.wenxin.service.service.ILoginLogService;
import cn.net.wenxin.service.service.IOperLogService;
import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.LogUtils;
import cn.net.wenxin.common.utils.ServletUtils;
import cn.net.wenxin.common.utils.StringUtils;
import cn.net.wenxin.common.utils.ip.AddressUtils;
import cn.net.wenxin.common.utils.ip.IpUtils;
import cn.net.wenxin.common.utils.spring.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author forum.wenxin.net.cn
 */
public class AppAsyncFactory {
    private static final Logger user_logger = LoggerFactory.getLogger("tb-user");

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     * @return 任务task
     */
    public static TimerTask recordLoginLog(final String username, final String status, final String message,
                                           final Object... args) {
        final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(status));
                s.append(LogUtils.getBlock(message));
                // 打印信息到日志
                user_logger.info(s.toString(), args);
                // 获取客户端操作系统
                String os = userAgent.getOperatingSystem().getName();
                // 获取客户端浏览器
                String browser = userAgent.getBrowser().getName();
                // 封装对象
                LoginLog logininfor = new LoginLog();
                logininfor.setUserName(username);
                logininfor.setIpaddr(ip);
                logininfor.setLoginLocation(address);
                logininfor.setBrowser(browser);
                logininfor.setOs(os);
                logininfor.setMsg(message);
                // 日志状态
                if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
                    logininfor.setStatus(Constants.SUCCESS);
                } else if (Constants.LOGIN_FAIL.equals(status)) {
                    logininfor.setStatus(Constants.FAIL);
                }
                // 插入数据
                SpringUtils.getBean(ILoginLogService.class).insertLoginLog(logininfor);
            }
        };
    }

    /**
     * @param username 用户名
     * @param type     业务类型（0其它 1新增 2修改 3删除）
     * @param explain  说明
     * @return
     */
    public static TimerTask recordOper(final String username, final int type, final String explain,final Object... args) {
        final String ip = IpUtils.getIpAddr();
        return new TimerTask() {
            @Override
            public void run() {
                String address = AddressUtils.getRealAddressByIP(ip);
                StringBuilder s = new StringBuilder();
                s.append(LogUtils.getBlock(ip));
                s.append(address);
                s.append(LogUtils.getBlock(username));
                s.append(LogUtils.getBlock(type));
                s.append(LogUtils.getBlock(explain));
                // 打印信息到日志
                user_logger.info(s.toString(), args);

                OperLog operLog = new OperLog();
                operLog.setUserName(username);
                operLog.setBusinessType(type);
                operLog.setTitle(explain);
                // 远程查询操作地点
                operLog.setOperLocation(address);
                operLog.setOperIp(ip);
                operLog.setOperTime(DateUtils.getNowDate());
                SpringUtils.getBean(IOperLogService.class).insertOperLog(operLog);
            }
        };
    }
}
