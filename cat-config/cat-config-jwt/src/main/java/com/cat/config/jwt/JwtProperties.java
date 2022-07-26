package com.cat.config.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

/**
 * jwt配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    /**
     * jwt的签名密钥
     * 用于校验主体信息是否被篡改
     */
    private String signingKey;

    /**
     * token的有效时间
     */
    private Duration expiration = Duration.ofMinutes(60);

    /**
     * 当前时间距离过期时间小于 「refreshPoint」 触发 刷新jwt 并且设置请求头为refreshToken携带给客户端
     */
    private Duration refreshPoint = Duration.ofMinutes(30);

    /**
     * 30分钟内快过期就刷新token
     */
    private String refreshTokenHeader = "refresh_token";
}
