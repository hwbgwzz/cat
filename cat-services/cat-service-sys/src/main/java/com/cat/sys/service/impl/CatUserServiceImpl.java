package com.cat.sys.service.impl;

import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.sys.entity.CatUser;
import com.cat.sys.repository.UserRepository;
import com.cat.sys.service.CatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatUserServiceImpl implements CatUserService {

    private final UserRepository userRepository;

    @Override
    public CatUserVO getCatUserByUserName(String userName) {
        CatUserVO catUserVO = null;
        Optional<CatUser> catUserOption = userRepository.findUserByUserName(userName);
        if (catUserOption.isPresent()) {
            catUserVO = new CatUserVO();
            BeanUtils.copyProperties(catUserVO, catUserOption.get());
        }
        return catUserVO;
    }
}
