package com.cat.security.auth;

import com.cat.security.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class FormAuthenticationManager implements ReactiveAuthenticationManager {

    private final UserDetailsServiceImpl userDetailsService;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        String userName = authentication.getName();
        Object obj = authentication;
        Mono<UserDetails> userDetailsMono = userDetailsService.findByUsername(userName);
        return null;
    }
}
