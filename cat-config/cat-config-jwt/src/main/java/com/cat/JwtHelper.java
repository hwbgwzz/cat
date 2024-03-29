package com.cat;

import com.cat.common.toolkit.context.SpringContextUtils;
import com.cat.common.toolkit.token.JwtTokenUtil;
import com.cat.config.jwt.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * 用配置初始化JwtTokenUtil实例
 */
@Component
@RequiredArgsConstructor
public class JwtHelper {
    private final JwtProperties jwtProperties;

    /**
     * bean被spring容器管理的场景使用
     * @return
     */
    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return JwtTokenUtil.builder()
                .secretKey(jwtProperties.getSigningKey())
                .expiration(jwtProperties.getExpiration().toMillis())
                .build();
    }

    /**
     * 非spring容器管理的类获取JwtTokenUtil
     * @return
     */
    public static JwtTokenUtil getJwtTokenUtil() {
        return SpringContextUtils.getBean(JwtTokenUtil.class);
    }

    /**
     * 非spring容器管理的类获取JwtProperties
     * @return
     */
    public static JwtProperties getJwtProperties() {
        return SpringContextUtils.getBean(JwtProperties.class);
    }
}
