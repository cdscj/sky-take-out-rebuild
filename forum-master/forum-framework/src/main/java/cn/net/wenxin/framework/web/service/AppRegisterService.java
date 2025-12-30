package cn.net.wenxin.framework.web.service;

import cn.net.wenxin.service.domain.User;
import cn.net.wenxin.service.mapper.UserMapper;
import cn.net.wenxin.service.service.IUserService;
import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.constant.UserConstants;
import cn.net.wenxin.common.core.domain.model.RegisterBody;
import cn.net.wenxin.common.core.redis.RedisCache;
import cn.net.wenxin.common.exception.user.CaptchaException;
import cn.net.wenxin.common.exception.user.CaptchaExpireException;
import cn.net.wenxin.common.utils.*;
import cn.net.wenxin.framework.manager.AsyncManager;
import cn.net.wenxin.framework.manager.factory.AppAsyncFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

/**
 * 注册校验方法
 *
 * @author forum.wenxin.net.cn
 */
@Component
public class AppRegisterService {
    @Autowired
    private IUserService userService;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserMapper userMapper;

    /**
     * 注册
     */
    @Transactional(rollbackFor = Exception.class)
    public String register(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        User user = new User();
        user.setUserName(username);
        validateCaptcha(username, registerBody.getCode());

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (username.length() < UserConstants.USERNAME_MIN_LENGTH
                || username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            msg = "账户长度必须在2到20个字符之间";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else if (!userService.checkUserNameUnique(user)) {
            msg = "保存用户'" + username + "'失败，注册账号已存在";
        } else {
            if (StringUtils.isPhone(username)) {
                user.setPhonenumber(username);
            } else if (StringUtils.isEmail(username)) {
                user.setEmail(username);
            }
            user.setNickName(username);
            user.setPassword(SecurityUtils.encryptPassword(password));
            boolean regFlag = userService.registerUser(user);
            if (!regFlag) {
                msg = "注册失败,请联系系统管理人员";
            } else {
                AsyncManager.me().execute(AppAsyncFactory.recordLoginLog(username, Constants.REGISTER, MessageUtils.message("user.register.success")));
            }
        }
        return msg;
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @return 结果
     */
    public void validateCaptcha(String username, String code) {
        String key = "";
        if (StringUtils.isPhone(username)) {
            key = Constants.SMS_CAPTCHA_REGISTER;
        } else if (StringUtils.isEmail(username)) {
            key = Constants.EMAIL_CODE;
        }
        String verifyKey = key + StringUtils.nvl(username, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        if (captcha == null) {
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            throw new CaptchaException();
        }
        redisCache.deleteObject(verifyKey);
    }

    public String getCode(String username) {
        String msg = "";
        String code = "";
        String cacheKey = "";
        if(StringUtils.isPhone(username)){
            cacheKey = Constants.SMS_CAPTCHA_REGISTER;
        }else if(StringUtils.isEmail(username)){
            cacheKey = Constants.EMAIL_CODE;
        }
        String rcode = redisCache.getCacheObject(cacheKey+username);
        if(StringUtils.isNotBlank(rcode)){
            return msg;
        }else{
            if(StringUtils.isPhone(username)){
                code = SMSUtils.SMSSend(username);
            }else if(StringUtils.isEmail(username)){
                code = SMSUtils.sendMail(username);
            }
            if(StringUtils.isBlank(code)){
                return "发送验证码失败";
            }else{
                redisCache.setCacheObject(cacheKey + username, code, Constants.SMS_CAPTCHA_EXPIRATION, TimeUnit.MINUTES);
            }
        }
        return msg;
    }

    public String resetPwd(RegisterBody registerBody) {
        String msg = "", username = registerBody.getUsername(), password = registerBody.getPassword();
        validateCaptcha(username, registerBody.getCode());

        if (StringUtils.isEmpty(username)) {
            msg = "用户名不能为空";
        } else if (StringUtils.isEmpty(password)) {
            msg = "用户密码不能为空";
        } else if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            msg = "密码长度必须在5到20个字符之间";
        } else {
            User ouser = userService.selectUserByUserName(username);
            if(ouser == null){
                msg = "用户名不存在";
            }else{
                if ("1".equals(ouser.getStatus())) {
                    msg = "用户已停用";
                }else{
                    ouser.setPassword(SecurityUtils.encryptPassword(password));
                    ouser.setUpdateTime(DateUtils.getNowDate());
                    userMapper.updateUser(ouser);
                    AsyncManager.me().execute(AppAsyncFactory.recordOper(ouser.getUserName(), 2, "重置密码"));
                }
            }
        }
        return msg;
    }

}
