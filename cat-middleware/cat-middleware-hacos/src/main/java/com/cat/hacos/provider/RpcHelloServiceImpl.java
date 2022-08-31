package com.cat.hacos.provider;

import com.cat.hacos.api.IRpcHelloService;

public class RpcHelloServiceImpl implements IRpcHelloService {
    @Override
    public String hello(String name) {
        return "Hello" + name + "!";
    }
}
