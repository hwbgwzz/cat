package com.cat.common.client.sys;

import com.cat.common.bean.sys.vo.CatUserVO;
import org.springframework.stereotype.Component;

@Component
public class SysClientFallback implements SysClient {

    @Override
    public CatUserVO getCatUserByUserName(String userName) {

        return new CatUserVO();
    }
}
