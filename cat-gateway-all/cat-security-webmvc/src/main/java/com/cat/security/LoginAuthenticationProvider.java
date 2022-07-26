package com.cat.security;

import cn.hutool.core.util.ObjectUtil;
import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.common.client.sys.SysClient;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 自定义身份验证组件,先filter创建认证对象传给LoginAuthenticationProvider
 */
@Component
@RequiredArgsConstructor
public class LoginAuthenticationProvider implements AuthenticationProvider {
    private final SysClient sysClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String passWord = authentication.getCredentials().toString();

        CatUserVO catUserVO = sysClient.getCatUserByUserName(userName);
        if (ObjectUtil.isNotEmpty(catUserVO)
                && passwordEncoder.encode(passWord).equals(catUserVO.getPassword())) {
            //生成认证令牌
            Authentication auth = new UsernamePasswordAuthenticationToken(userName, passWord);
            return auth;
        }else {
            throw new BadCredentialsException("用户名或密码错误");
        }
    }

    /**
     * 是否可以提供输入类型的认证服务
     * @param authentication
     * @return
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
