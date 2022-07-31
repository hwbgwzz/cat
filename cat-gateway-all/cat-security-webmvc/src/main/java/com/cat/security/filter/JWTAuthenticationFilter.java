package com.cat.security.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import com.cat.JwtHelper;
import com.cat.common.bean.Result;
import com.cat.common.constant.JwtConstant;
import com.cat.common.security.UserSecurityInfo;
import com.cat.common.toolkit.ObjectUtil;
import com.cat.common.toolkit.json.JSON;
import com.cat.config.jwt.JwtProperties;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 请求接口资源的JWT验证
 */
public class JWTAuthenticationFilter extends GenericFilterBean {

    private static final JwtProperties jwtProperties = JwtHelper.getJwtProperties();

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain filterChain) throws IOException, ServletException {
        String token = ((HttpServletRequest)req).getHeader(JwtConstant.HEADER_STRING);
        if (StringUtils.hasText(token)) {
            token = token.replace(JwtConstant.TOKEN_PREFIX, "");
            boolean valid  = JwtHelper.getJwtTokenUtil().verifyClient(token);
            if (valid) {
                Claims claims = JwtHelper.getJwtTokenUtil().getBody(token);
                UserSecurityInfo userSecurityInfo = JwtHelper.getJwtTokenUtil().parseClaims(claims, UserSecurityInfo.class);
                if (ObjectUtil.isEmptyObject(userSecurityInfo)) {
                    tokenInvalidHandler((HttpServletResponse)res, "token中的用户认证信息解析失败");
                    return;
                }
                //获取用户认证信息并创建认证token对象
                createCertificationTokenObject(userSecurityInfo);
                 //30分钟内快过期启用自动续期刷新token
                String refreshToken = willExpirerDemandRefreshToken(userSecurityInfo, claims);
                if (StrUtil.isNotBlank(refreshToken)) {
                    setHeaderRefreshToken((HttpServletResponse) res, refreshToken);
                }

                filterChain.doFilter(req,res);
            } else {
                //token校验失败，返回客户端
                tokenInvalidHandler((HttpServletResponse)res, "token校验失败");
            }
        } else {
            filterChain.doFilter(req,res);
            //没有token，返回客户端
            //tokenInvalidHandler((HttpServletResponse)res, "没有token");
        }
    }

    private void setHeaderRefreshToken(HttpServletResponse res, String refreshToken) {
        res.setHeader(jwtProperties.getRefreshTokenHeader(), JwtConstant.TOKEN_PREFIX + refreshToken);
    }

    private void createCertificationTokenObject(UserSecurityInfo userSecurityInfo) {
        //生成认证令牌
        Authentication auth = new UsernamePasswordAuthenticationToken(userSecurityInfo, userSecurityInfo.isAuthenticated());
        //认证上下文设置认证令牌
        auth.setAuthenticated(userSecurityInfo.isAuthenticated());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    private String willExpirerDemandRefreshToken(UserSecurityInfo userSecurityInfo, Claims claims) {
        if (claims.getExpiration().getTime() - System.currentTimeMillis() < jwtProperties.getRefreshPoint().toMillis()) {
            String refreshToken = JwtHelper.getJwtTokenUtil().generateToken(JSON.toJSONString(userSecurityInfo), userSecurityInfo.getUserId());
            return refreshToken;
        }
        return null;
    }

    private void tokenInvalidHandler(HttpServletResponse response, String errorMsg) {
        response_Fail(response, Result.error(errorMsg));
    }

    private void response_Fail(HttpServletResponse response, Result result) {
        try {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.HTTP_OK);
            response.getOutputStream().println(JSON.toFormatJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
