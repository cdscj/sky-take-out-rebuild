package cn.net.wenxin.framework.web.service;

import cn.net.wenxin.service.domain.User;
import cn.net.wenxin.service.service.IUserService;
import cn.net.wenxin.common.constant.Constants;
import cn.net.wenxin.common.core.domain.entity.AppUser;
import cn.net.wenxin.common.core.domain.model.LoginUser;
import cn.net.wenxin.common.enums.UserStatus;
import cn.net.wenxin.common.exception.base.BaseException;
import cn.net.wenxin.common.utils.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 用户验证处理
 *
 * @author ruoyi
 */
@Service("appUserDetailsService")
public class AppUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(AppUserDetailsService.class);

    @Autowired
    private IUserService userService;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已停用");
        }
        AppUser appUser = new AppUser();
        appUser.setId(user.getId());
        appUser.setUserName(user.getUserName());
        appUser.setPassword(user.getPassword());
        return createLoginUser(appUser);
    }

    public UserDetails createLoginUser(AppUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setLoginType(Constants.LOGIN_TYPE_APP);
        loginUser.setAppUser(user);
        return loginUser;
    }
}
