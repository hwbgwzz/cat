package com.cat.hacos.provider;

import com.cat.hacos.api.IRpcService;

public class RpcServiceImpl implements IRpcService {

    @Override
    public int add(int a, int b) {
        return a + b;
    }
}
