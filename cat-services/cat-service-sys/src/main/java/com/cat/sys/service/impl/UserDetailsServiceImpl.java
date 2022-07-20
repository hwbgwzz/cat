package com.cat.sys.service.impl;

import com.cat.sys.entity.CatUser;
import com.cat.sys.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        CatUser user = userRepository.findUserByUserName(userName)
                .orElseThrow(() -> new UsernameNotFoundException("user not present"));
        return user;
    }

    public void createUser(UserDetails user) {
        userRepository.save((CatUser) user);
    }
}
