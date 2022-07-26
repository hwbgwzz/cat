package com.cat;

import com.cat.common.toolkit.token.JwtTokenUtil;
import com.cat.config.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 用配置初始化JwtTokenUtil实例
 */
@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final JwtProperties jwtProperties;

    private JwtTokenUtil jwtTokenUtil;

    @PostConstruct
    private void initUtil() {
        jwtTokenUtil = JwtTokenUtil.builder()
                .secretKey(jwtProperties.getSigningKey())
                .expiration(jwtProperties.getExpiration().toMillis())
                .build();
    }

    public JwtTokenUtil getJwtTokenUtil() {
        return jwtTokenUtil;
    }
}
