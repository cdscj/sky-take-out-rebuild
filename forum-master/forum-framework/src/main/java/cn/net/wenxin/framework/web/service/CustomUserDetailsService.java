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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于加载用户信息 实现UserDetailsService接口，或者实现AuthenticationUserDetailsService接口
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
    private static final Logger log = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Autowired
    private IUserService userService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 结合具体的逻辑去实现用户认证，并返回继承UserDetails的用户对象;
        User user = userService.selectUserByUserName(username);
        if (StringUtils.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UsernameNotFoundException("登录用户：" + username + " 不存在");
        } else if (UserStatus.DELETED.getCode().equals(user.getDelFlag())) {
            log.info("登录用户：{} 已被删除.", username);
            throw new BaseException("对不起，您的账号：" + username + " 已被删除");
        } else if (!"0".equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new BaseException("对不起，您的账号：" + username + " 不是正常状态");
        }
        AppUser appUser = new AppUser();
        appUser.setId(user.getId());
        appUser.setUserName(user.getUserName());
        appUser.setPassword(user.getPassword());

        LoginUser loginUser = new LoginUser();
        loginUser.setLoginType(Constants.LOGIN_TYPE_APP);
        loginUser.setAppUser(appUser);
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        authorities.add(authority);
        return loginUser;
    }
}
