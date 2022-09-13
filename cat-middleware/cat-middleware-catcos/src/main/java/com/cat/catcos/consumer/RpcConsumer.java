package com.cat.catcos.consumer;

import com.cat.catcos.api.IRpcHelloService;
import com.cat.catcos.api.IRpcService;
import com.cat.catcos.consumer.proxy.RpcProxy;
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
