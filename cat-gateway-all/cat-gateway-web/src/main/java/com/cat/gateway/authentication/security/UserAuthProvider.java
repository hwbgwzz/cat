package com.cat.gateway.authentication.security;

import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.common.client.sys.SysClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserAuthProvider implements AuthenticationProvider {
    //尝试认证次数
    private static final int ATTEMPTS_LIMIT = 3;

    private final SysClient sysClient;

    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        Object object = authentication.getPrincipal();
        String userName = authentication.getName();
        authentication.setAuthenticated(true);
        CatUserVO catUserVO = sysClient.getCatUserByUserName(userName);
        return authentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return true;
    }
}
