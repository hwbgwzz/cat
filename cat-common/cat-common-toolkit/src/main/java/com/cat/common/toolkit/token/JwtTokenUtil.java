package com.cat.common.toolkit.token;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.symmetric.DES;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.json.JSONUtil;
import io.jsonwebtoken.*;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.ObjectUtils;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JSON WEB TOKEN工具类
 * @description 生成 token，以及刷新 token，以及验证 token
 */
@Slf4j
@Builder
public class JwtTokenUtil {
    //jwt签名密钥
    private String secretKey;

    //过期时长
    private long expiration;

    //设置属性key
    private static final String claim_key = "body";

    //对body信息进行编码
    private static DES des;

    static {
        des = SecureUtil.des(
                SecureUtil.generateKey(SymmetricAlgorithm.DES.getValue(), "___AWKKWJ$.&".getBytes(StandardCharsets.UTF_8))
                        .getEncoded());
    }

    /**
     * 生成token
     * @param jsonBody
     * @param subject
     * @return
     */
    private String generateToken(String jsonBody, String subject) {
        Date nowDate = new Date();
        Date expirationDate = new Date(nowDate.getTime() + expiration);
        return Jwts.builder()
                .claim(claim_key, des.encryptHex(jsonBody))
                .setSubject(subject)
                .setIssuedAt(nowDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }

    /**
     * 校验jwt签名，校验jwt令牌是否合法
     */
    public  boolean verifyClient(String token) {
        try {
            Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }

    /**
     * 解析jwt中的内容
     */
    public Claims getBody(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 这里是jwt中的信息被des二次加密了, 要解密
     * 避免泄漏用户的信息
     */
    public <T> T parseClaims(Claims claims, Class<T> t) {

        try {
            if (ObjectUtils.isEmpty(claims)) {
                return null;
            }

            String token = (String) claims.get(claim_key);

            if (ObjectUtils.isEmpty(token)) {
                return null;
            }

            String decryptStr = des.decryptStr(token);

            return JSONUtil.toBean(decryptStr, t);

        } catch (Exception e) {
            log.info("error parseClaims {} ", e.getMessage(), e);
            return null;
        }
    }
}
