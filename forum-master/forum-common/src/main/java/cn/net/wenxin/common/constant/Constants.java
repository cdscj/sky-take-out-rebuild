package cn.net.wenxin.common.constant;

import io.jsonwebtoken.Claims;

/**
 * 通用常量信息
 * 
 * @author forum.wenxin.net.cn
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * www主域
     */
    public static final String WWW = "www.";

    /**
     * http请求
     */
    public static final String HTTP = "http://";

    /**
     * https请求
     */
    public static final String HTTPS = "https://";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";
 
    /**
     * 验证码有效期（分钟）
     */
    public static final Integer CAPTCHA_EXPIRATION = 2;

    /**
     * 短信验证码前缀（注册）
     */
    public static final String SMS_CAPTCHA_REGISTER = "sms_captcha_register:";
    /**
     * 短信验证码前缀（修改密码）
     */
    public static final String SMS_CAPTCHA_CHANGEPWD = "sms_captcha_changepwd:";

    /**
     * 短信验证码前缀（修改用户名）
     */
    public static final String SMS_CAPTCHA_CHANGEUSER = "sms_captcha_changeuser:";

    public static final String EMAIL_BOUND = "email_bound:";
    /**
     * 邮箱验证码前缀
     */
    public static final String EMAIL_CODE = "email_code:";
    /**
     * 短信验证码有效期（分钟）
     */
    public static final Integer SMS_CAPTCHA_EXPIRATION = 5;
    /**
     * 用户类型(后台管理)
     */
    public static final String LOGIN_TYPE_MANAGER = "login_type_manager";
    /**
     * 用户类型(APP)
     */
    public static final String LOGIN_TYPE_APP = "login_type_app";
    /**
     * 用户类型
     */
    public static final String LOGIN_TYPE_KEY= "login_type";

    /**
     * 令牌
     */
    public static final String TOKEN = "token";

    /**
     * 令牌前缀
     */
    public static final String TOKEN_PREFIX = "Bearer ";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_USER_KEY = "login_user_key";

    /**
     * 令牌前缀
     */
    public static final String LOGIN_APP_USER_KEY = "login_app_user_key";

    /**
     * 用户ID
     */
    public static final String JWT_USERID = "userid";

    /**
     * 用户名称
     */
    public static final String JWT_USERNAME = Claims.SUBJECT;

    /**
     * 用户头像
     */
    public static final String JWT_AVATAR = "avatar";

    public static final String EMAIL_CONTENT = "【文心科技】 您的验证码：code,请不要把验证码泄露给其它人！5分钟内有效。";

    /**
     * 创建时间
     */
    public static final String JWT_CREATED = "created";

    /**
     * 用户权限
     */
    public static final String JWT_AUTHORITIES = "authorities";

    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * RMI 远程方法调用
     */
    public static final String LOOKUP_RMI = "rmi:";

    /**
     * LDAP 远程方法调用
     */
    public static final String LOOKUP_LDAP = "ldap:";

    /**
     * LDAPS 远程方法调用
     */
    public static final String LOOKUP_LDAPS = "ldaps:";

    /**
     * 定时任务白名单配置（仅允许访问的包名，如其他需要可以自行添加）
     */
    public static final String[] JOB_WHITELIST_STR = { "cn.net.wenxin" };

    /**
     * 定时任务违规的字符
     */
    public static final String[] JOB_ERROR_STR = { "java.net.URL", "javax.naming.InitialContext", "org.yaml.snakeyaml",
            "org.springframework", "org.apache", "cn.net.wenxin.common.utils.file", "cn.net.wenxin.common.config" };
}
