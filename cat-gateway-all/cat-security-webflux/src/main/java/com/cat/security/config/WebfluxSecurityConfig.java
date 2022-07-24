package com.cat.security.config;

import com.cat.security.auth.FormAuthenticationManager;
import com.cat.security.auth.TokenAuthenticationManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity  //开启security配置
@RequiredArgsConstructor
public class WebfluxSecurityConfig {

    private final FormAuthenticationManager formAuthenticationManager;

    private final TokenAuthenticationManager tokenAuthenticationManager;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity httpSecurity) {
        httpSecurity
                .addFilterAt(null, null)
                .formLogin(formLoginSpec -> formLoginSpec
                        .loginPage("/login")
                        .authenticationEntryPoint(null)
                        .authenticationManager(formAuthenticationManager)
                        .authenticationSuccessHandler(null)
                        .authenticationFailureHandler(null)
                )
                .oauth2Login(oAuth2LoginSpec -> oAuth2LoginSpec
                        .authenticationConverter(null)
                        .authenticationSuccessHandler(null)
                        .authenticationFailureHandler(null)
                )
                .authorizeExchange(exchange  -> exchange
                        .pathMatchers("/login", "/logout").permitAll()
                        .pathMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyExchange().access(null)
                )
                .authenticationManager(tokenAuthenticationManager)
                .securityContextRepository(null)
                .exceptionHandling()
                .authenticationEntryPoint(null)
                .accessDeniedHandler(null)
                .and()
                .logout(logoutSpec -> logoutSpec
                        .logoutUrl("/logout")
                        .logoutHandler(null)
                )
                .cors().disable()
                .csrf().disable()
        ;
        return httpSecurity.build();
    }
}
