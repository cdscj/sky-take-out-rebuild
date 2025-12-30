package cn.net.wenxin.framework.security.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class AppAuthenticationToken  extends UsernamePasswordAuthenticationToken {
    public AppAuthenticationToken(Object principal, Object credentials){
        super(principal,credentials);
    }
}
