package cn.net.wenxin.framework.security.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class ManagerAuthenticationToken extends UsernamePasswordAuthenticationToken {
    public ManagerAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
