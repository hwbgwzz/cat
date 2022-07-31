package com.cat.security.config;

import com.cat.security.auth.FormAuthenticationManager;
import com.cat.security.filter.JWTAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.savedrequest.NoOpServerRequestCache;

import static org.springframework.http.HttpMethod.GET;

@EnableWebFluxSecurity  //开启security配置
@RequiredArgsConstructor
public class WebfluxSecurityConfig {

    private final FormAuthenticationManager formAuthenticationManager;

    private final JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                //认证成功后会返回JWT给客户端，验证客户端请求头部的JWT
                .addFilterBefore(jwtAuthenticationFilter, SecurityWebFiltersOrder.LAST)
                //.addFilterAfter(null, null)
                //form表单登录
                .formLogin(formLoginSpec -> formLoginSpec
                        .loginPage("/login")
                        .authenticationEntryPoint(null)
                        .authenticationManager(formAuthenticationManager)
                        .authenticationSuccessHandler(null)
                        .authenticationFailureHandler(null)
                )
                //oauth2登录
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        .authenticationManager(null)
                        .authenticationSuccessHandler(null)
                        .authenticationFailureHandler(null)
                )
                //哪些请求放行
                .authorizeExchange(exchange  -> exchange
                        // swagger 静态资源无需授权
                        .pathMatchers(GET, "/**/swagger-ui.html", "/**/swagger-resources/**", "/**/webjars/**", "/**/*/api-docs",  "/actuator/**", "/**/doc.html", "/**/swagger-resources/**", "/favicon.ico").permitAll()
                        .pathMatchers("/login", "/logout").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        //其它请求都要认证
                        .anyExchange().authenticated()
                )
                .exceptionHandling()
                //AuthenticationException,认证出错的处理器
                .authenticationEntryPoint(null)
                //访问的接口无权限的处理器
                .accessDeniedHandler(null)
                .and()
                .logout(logoutSpec -> logoutSpec
                        .logoutUrl("/logout")
                        //.logoutHandler(null)
                        .logoutSuccessHandler(null)
                )
                //关闭跨域验证，允许跨域
                .cors().disable()
                //关闭跨站验证，允许跨站
                .csrf().disable()
                .requestCache()
                // jwt 无状态 不需要缓存请求信息
                .requestCache(NoOpServerRequestCache.getInstance())
        ;
        return httpSecurity.build();
    }
}
