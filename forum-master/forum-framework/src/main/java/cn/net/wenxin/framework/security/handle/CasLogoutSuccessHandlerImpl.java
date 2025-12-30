package cn.net.wenxin.framework.security.handle;

import cn.net.wenxin.service.domain.LoginLog;
import cn.net.wenxin.service.service.ILoginLogService;
import cn.net.wenxin.common.core.domain.AjaxResult;
import cn.net.wenxin.common.core.domain.model.LoginUser;
import cn.net.wenxin.common.utils.ServletUtils;
import cn.net.wenxin.common.utils.StringUtils;
import cn.net.wenxin.common.utils.ip.AddressUtils;
import cn.net.wenxin.common.utils.ip.IpUtils;
import cn.net.wenxin.framework.web.service.TokenService;
import com.alibaba.fastjson2.JSON;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * 自定义退出处理类 返回成功
 *
 * @author ruoyi
 */
@Configuration
public class CasLogoutSuccessHandlerImpl implements LogoutSuccessHandler {
    @Autowired
    private TokenService tokenService;
    @Autowired
    private ILoginLogService loginLogService;
    /**
     * 退出处理
     *
     * @return
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        LoginUser loginUser = tokenService.getLoginUser(request);
        if (StringUtils.isNotNull(loginUser)) {
            String userName = loginUser.getUsername();
            // 删除用户缓存记录
            tokenService.delAppLoginUser(loginUser.getToken());
            // 记录用户退出日志
            addLoginLog(userName,"退出成功","0","");
        }
        ServletUtils.renderString(response, JSON.toJSONString(AjaxResult.success("退出成功")));
    }

    /**
     * 退出日志
     */
    private void addLoginLog(String userName,String msg,String status,String service) {
        UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
        String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
        String address = AddressUtils.getRealAddressByIP(ip);
        // 获取客户端操作系统
        String os = userAgent.getOperatingSystem().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();

        LoginLog log = new LoginLog();
        log.setUserName(userName);
        log.setIpaddr(ip);
        log.setLoginLocation(address);
        log.setBrowser(browser);
        log.setOs(os);
        log.setMsg(msg);
        log.setStatus(status);
        log.setService(service);
        log.setLoginTime(new Date());
        loginLogService.insertLoginLog(log);
    }
}
