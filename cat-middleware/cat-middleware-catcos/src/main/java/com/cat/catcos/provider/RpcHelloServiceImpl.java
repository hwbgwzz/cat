package com.cat.catcos.provider;

import com.cat.catcos.api.IRpcHelloService;

public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello" + name + "!";
    }
}
