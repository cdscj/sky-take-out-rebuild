package cn.net.wenxin.framework.web.service;

import javax.annotation.Resource;

import cn.net.wenxin.common.constant.CacheConstants;
import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.constant.UserConstants;
import cn.net.wenxin.common.exception.ServiceException;
import cn.net.wenxin.framework.security.provider.ManagerAuthenticationToken;
import cn.net.wenxin.system.mapper.SysUserMapper;
import cn.net.wenxin.system.service.ISysConfigService;
import cn.net.wenxin.system.service.ISysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import cn.net.wenxin.common.core.domain.entity.SysUser;
import cn.net.wenxin.common.core.domain.model.LoginUser;
import cn.net.wenxin.common.core.redis.RedisCache;
import cn.net.wenxin.common.exception.user.BlackListException;
import cn.net.wenxin.common.exception.user.CaptchaException;
import cn.net.wenxin.common.exception.user.CaptchaExpireException;
import cn.net.wenxin.common.exception.user.UserNotExistsException;
import cn.net.wenxin.common.exception.user.UserPasswordNotMatchException;
import cn.net.wenxin.common.utils.DateUtils;
import cn.net.wenxin.common.utils.MessageUtils;
import cn.net.wenxin.common.utils.StringUtils;
import cn.net.wenxin.common.utils.ip.IpUtils;
import cn.net.wenxin.framework.manager.AsyncManager;
import cn.net.wenxin.framework.manager.factory.AsyncFactory;
import cn.net.wenxin.framework.security.context.AuthenticationContextHolder;

import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * 登录校验方法
 * 
 * @author forum.wenxin.net.cn
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private SysUserMapper userMapper;

    @Autowired
    private ISysConfigService configService;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        validateCaptcha(username, code, uuid);
        // 登录前置校验
        loginPreCheck(username, password);

        // 用户验证
        Authentication authentication = null;
        try {
            authentication = login(new ManagerAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        changeLoginTime(loginUser);
        // 生成token
        return tokenService.createToken(loginUser, Constants.LOGIN_TYPE_MANAGER);
    }


    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code 验证码
     * @param uuid 唯一标识
     * @return 结果
     */
    public void validateCaptcha(String username, String code, String uuid)
    {
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        if (captchaEnabled)
        {
            String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
            String captcha = redisCache.getCacheObject(verifyKey);
            redisCache.deleteObject(verifyKey);
            if (captcha == null)
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.expire")));
                throw new CaptchaExpireException();
            }
            if (!code.equalsIgnoreCase(captcha))
            {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.jcaptcha.error")));
                throw new CaptchaException();
            }
        }
    }

    /**
     * 登录前置校验
     * @param username 用户名
     * @param password 用户密码
     */
    public void loginPreCheck(String username, String password)
    {
        // 用户名或密码为空 错误
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("not.null")));
            throw new UserNotExistsException();
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // 用户名不在指定范围内 错误
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH)
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
            throw new UserPasswordNotMatchException();
        }
        // IP黑名单校验
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr()))
        {
            AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("login.blocked")));
            throw new BlackListException();
        }
    }

    /**
     * 校验账号密码并进行登陆
     *
     * @param upToken
     */
    private Authentication login(UsernamePasswordAuthenticationToken upToken) {
        //验证
        Authentication authentication = authenticationManager.authenticate(upToken);
        //将用户信息保存到SecurityContextHolder=登陆
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    /**
     * 修改用户最后登录时间
     *
     * @param loginUser
     */
    private void changeLoginTime(LoginUser loginUser) {
        SysUser user = loginUser.getUser();
        if (user != null && user.getUserId() != null) {
            user.setLoginIp(loginUser.getIpaddr());
            user.setLoginDate(new Date());
            userMapper.updateUser(user);
        }

    }

    public String getToken(String username, String password) {
        // 用户验证
        Authentication authentication = null;
        try {
            authentication = login(new ManagerAuthenticationToken(username, password));
        } catch (Exception e) {
            if (e instanceof BadCredentialsException) {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, MessageUtils.message("user.password.not.match")));
                throw new UserPasswordNotMatchException();
            } else {
                AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_FAIL, e.getMessage()));
                throw new ServiceException(e.getMessage());
            }
        }
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(username, Constants.LOGIN_SUCCESS, MessageUtils.message("user.login.success")));
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        changeLoginTime(loginUser);
        // 生成token
        return tokenService.createToken(loginUser, Constants.LOGIN_TYPE_MANAGER);
    }
}