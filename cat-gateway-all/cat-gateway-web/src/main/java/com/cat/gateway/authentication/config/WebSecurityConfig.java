package com.cat.gateway.authentication.config;

import com.cat.gateway.authentication.security.UserAuthProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
// @EnableGlobalMethodSecurity(prePostEnabled = true)  //  启用方法级别的权限认证
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final AuthenticationProvider userAuthProvider;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .cors().disable()
                .authorizeRequests().antMatchers("/register**")
                //.hasAnyRole("USER", "ADMIN")
                .permitAll()
                .and()
                .formLogin().loginPage("/login")
                //.successHandler(null)
                //.failureHandler(null)
                .permitAll()
                .and()
                .logout().invalidateHttpSession(true).clearAuthentication(true)
                //.addLogoutHandler(null)
                //.logoutSuccessHandler(null)
                .permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .authenticationProvider(userAuthProvider);
    }

/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationMgr) throws Exception {
        authenticationMgr.inMemoryAuthentication().withUser("test1").password("123456")
                .authorities("ROLE_USER").and().withUser("test2").password("123456")
                .authorities("ROLE_USER", "ROLE_ADMIN");
    }*/
}
