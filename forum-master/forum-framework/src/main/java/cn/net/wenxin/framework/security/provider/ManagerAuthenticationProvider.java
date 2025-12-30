package cn.net.wenxin.framework.security.provider;

import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsPasswordService;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

public class ManagerAuthenticationProvider  extends DaoAuthenticationProvider {
    /**
     * 初始化 将使用Manager专用的userDetailsService
     * @param encoder
     * @param userDetailsService
     */
    public ManagerAuthenticationProvider(PasswordEncoder encoder, UserDetailsService userDetailsService){
        setPasswordEncoder(encoder);
        setUserDetailsService(userDetailsService);
    }

    @Override
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }

    @Override
    public void setUserDetailsPasswordService(UserDetailsPasswordService userDetailsPasswordService) {
        super.setUserDetailsPasswordService(userDetailsPasswordService);
    }

    /**
     * 判断只有传入ManagerAuthenticationToken的时候才使用这个Provider
     * supports会在AuthenticationManager层被调用
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return ManagerAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
