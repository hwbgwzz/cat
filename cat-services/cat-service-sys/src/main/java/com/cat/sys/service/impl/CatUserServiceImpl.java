package com.cat.sys.service.impl;

import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.sys.entity.CatUser;
import com.cat.sys.service.CatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CatUserServiceImpl implements CatUserService {

    private final UserDetailsService userDetailsService;

    @Override
    public CatUserVO getCatUserByUserName(String userName) {
        CatUser user = (CatUser)userDetailsService.loadUserByUsername(userName);
        CatUserVO catUserVO = new CatUserVO();
        BeanUtils.copyProperties(catUserVO, user);
        return catUserVO;
    }
}
