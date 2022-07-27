package com.cat.security.config;

import com.cat.security.LoginAuthenticationProvider;
import com.cat.security.filter.JWTAuthenticationFilter;
import com.cat.security.filter.LoginAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
// @EnableGlobalMethodSecurity(prePostEnabled = true)  //  启用方法级别的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final LoginAuthenticationProvider loginAuthenticationProvider;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    /**
     * 设置 HTTP 安全验证规则
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                //关闭csrf验证
                .csrf().disable()
                //关闭cors验证
                .cors().disable()
                //对请求进行认证
                .authorizeRequests()
                .antMatchers("/").permitAll()
                //.antMatchers(HttpMethod.POST, "/login").permitAll()
                .and()
                .formLogin()
                .loginPage("/login")
                //.usernameParameter(null)
                //.passwordParameter(null)
                //.successHandler(null)
                //.failureHandler(null)
                .permitAll()
                //.and()
                //自定义身份验证组件
                //.authenticationProvider(new LoginAuthenticationProvider())
                .and()
                .logout()
                .logoutUrl("/logout").invalidateHttpSession(true).clearAuthentication(true)
                //.addLogoutHandler(null)
                //.logoutSuccessHandler(null)
                .permitAll()
                .and()
                //对其它的请求进行认证
                .authorizeRequests().anyRequest().authenticated()
                .and()
                //添加一个过滤器 把访问 /login 的请求交给 JWTLoginFilter 来处理 这个类认证成功后生成jwt给客户端
                .addFilterBefore(new LoginAuthenticationFilter("/login", authenticationManager()),
                        UsernamePasswordAuthenticationFilter.class)
                //添加一个过滤器验证客户端请求头带的Token是否合法，如果合法就可以访问接口资源
                .addFilterBefore(new JWTAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder authenticationMgb) throws Exception {
        // 使用自定义身份验证组件,先filter创建认证对象传给LoginAuthenticationProvider
        authenticationMgb.authenticationProvider(loginAuthenticationProvider);
        /*authenticationMgr.inMemoryAuthentication().withUser("test1").password("123456")
                .authorities("ROLE_USER").and().withUser("test2").password("123456")
                .authorities("ROLE_USER", "ROLE_ADMIN");*/
    }

}
