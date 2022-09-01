package com.cat.hacos.consumer;

import com.cat.hacos.api.IRpcHelloService;
import com.cat.hacos.api.IRpcService;
import com.cat.hacos.consumer.proxy.RpcProxy;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RpcConsumer {
    public static void main(String[] args) {
        IRpcHelloService rpcHello = RpcProxy.create(IRpcHelloService.class);

        log.info(rpcHello.hello("Tom 老师"));

        IRpcService iRpcService = RpcProxy.create(IRpcService.class);

        log.info("1 + 2 = {}", iRpcService.add(1, 2));
    }
}
