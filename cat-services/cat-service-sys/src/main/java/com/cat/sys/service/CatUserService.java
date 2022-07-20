package com.cat.sys.service;

import com.cat.common.bean.sys.vo.CatUserVO;

public interface CatUserService {
    CatUserVO getCatUserByUserName(String userName);
}
