package com.cat.catcos.provider;

import com.cat.catcos.api.IRpcService;

public class RpcServiceImpl implements IRpcService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
