package com.cat.security;

import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.common.client.sys.SysClient;
import com.cat.common.security.UserSecurityInfo;
import com.cat.common.toolkit.ObjectUtil;
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
public class IdentityAuthenticationProvider implements AuthenticationProvider {
    private final SysClient sysClient;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userName = authentication.getName();
        String passWord = authentication.getCredentials().toString();

        CatUserVO catUserVO = sysClient.getCatUserByUserName(userName);
        if (ObjectUtil.isEmpty(catUserVO)) {
            throw new BadCredentialsException("用户名错误");
        }
        if (passwordEncoder.encode(passWord).equals(catUserVO.getPassword())) {
            UserSecurityInfo userSecurityInfo = ObjectUtil.entityToModel(catUserVO, UserSecurityInfo.class);
            userSecurityInfo.setAuthenticated(true);
            //生成认证令牌
            Authentication auth = new UsernamePasswordAuthenticationToken(userSecurityInfo, userSecurityInfo.isAuthenticated());
            return auth;
        }
        throw new BadCredentialsException("密码错误");
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
