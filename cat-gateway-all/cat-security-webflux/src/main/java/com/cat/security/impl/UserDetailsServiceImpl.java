package com.cat.security.impl;

import cn.hutool.core.util.ObjectUtil;
import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.common.client.sys.SysClient;
import com.cat.security.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements ReactiveUserDetailsService {

    private final SysClient sysClient;

    @Override
    public Mono<UserDetails> findByUsername(String userName) {
        CatUserVO catUserVO = sysClient.getCatUserByUserName(userName);
        if (ObjectUtil.isNull(catUserVO)) {
            throw new UsernameNotFoundException("Username is not present");
        }
        SecurityUserDetails securityUserDetails = new SecurityUserDetails(
                catUserVO.getUserName(),
                catUserVO.getPassword(),
                true,
                catUserVO.isAccountNonLocked(),
                true,
                true, new ArrayList<>(),
                catUserVO.getUserId()
        );
        return Mono.just(securityUserDetails);
    }
}
