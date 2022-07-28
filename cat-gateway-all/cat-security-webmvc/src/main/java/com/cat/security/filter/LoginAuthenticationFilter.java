package com.cat.security.filter;

import cn.hutool.http.HttpStatus;
import com.cat.JwtHelper;
import com.cat.common.bean.Result;
import com.cat.common.constant.JwtConstant;
import com.cat.common.security.UserSecurityInfo;
import com.cat.common.toolkit.json.JSON;
import com.cat.security.domain.AccountCredentials;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录处理Filter
 */
public class LoginAuthenticationFilter extends AbstractAuthenticationProcessingFilter  {
    public LoginAuthenticationFilter(String url, AuthenticationManager authManager) {
        super(new AntPathRequestMatcher(url));
        //设置认证管理器
        setAuthenticationManager(authManager);
    }

    /**
     * 验证成功后
     * @param request
     * @param response
     * @param chain
     * @param authResult
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        UserSecurityInfo userSecurityInfo = (UserSecurityInfo)authResult.getPrincipal();
        String token = JwtHelper
                .getJwtTokenUtil()
                .generateToken(JSON.toJSONString(userSecurityInfo), userSecurityInfo.getUserId());
        Result<String> result = Result.OK("登录验证成功", token);
        // 将 JWT 写入 body
        try {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.HTTP_OK);
            response.setHeader(JwtConstant.HEADER_STRING, JwtConstant.TOKEN_PREFIX + token);
            response.getOutputStream().println(JSON.toFormatJSONString(result));

            //以上可通过response重定向(302)到系统首页并带上token
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 验证失败后
     * @param request
     * @param response
     * @param failed
     * @throws IOException
     * @throws ServletException
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        Result<?> result = Result.error("登录验证失败:" + failed.getMessage());
        // 将 JWT 写入 body
        try {
            response.setContentType("application/json");
            response.setStatus(HttpStatus.HTTP_OK);
            response.getOutputStream().println(JSON.toFormatJSONString(result));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     *  登录时解析登录数据生成验证令牌
     * @param req
     * @param res
     * @return
     * @throws AuthenticationException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException, IOException, ServletException {
        // JSON反序列化成 AccountCredentials
        AccountCredentials accountCredentials = JSON.getInstance().readValue(req.getInputStream(), AccountCredentials.class);
        //返回验证令牌
        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        accountCredentials.getUsername(),
                        accountCredentials.getPassword()
                )
        );
    }
}
