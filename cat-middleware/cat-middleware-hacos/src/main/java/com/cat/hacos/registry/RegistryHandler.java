package com.cat.hacos.registry;

import com.cat.hacos.protocol.InvokerProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class RegistryHandler extends ChannelInboundHandlerAdapter {
    public static ConcurrentHashMap<String, Object> registryMap = new ConcurrentHashMap<>(12);

    private List<String> classNames = new ArrayList<String>();

    public RegistryHandler() {
        scannerClass("com.cat.hacos.provider");
        doRegister();
    }

    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader()
                .getResource(packageName.replaceAll("\\.","/"));
        log.info(url.getPath());
        File dir = new File(url.getFile());
        for (File file : dir.listFiles()) {
            if (file.isDirectory()) {
                scannerClass(packageName + "." + file.getName());
            } else {
                classNames.add(packageName + "." + file.getName().replace(".class","").trim());
            }
        }
    }

    private void doRegister() {
        if (classNames.size() ==0) {return;}
        for (String className : classNames) {
            try {
                Class<?> clazz = Class.forName(className);
                Class<?> l = clazz.getInterfaces()[0];
                registryMap.put(l.getName(), clazz.newInstance());
            } catch (Exception e) {
                log.error(e.getMessage(), e);
                e.printStackTrace();
            }
        }
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelRegistered:"+ctx.name());
        super.channelRegistered(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        log.info("channelUnregistered:"+ctx.name());
        super.channelUnregistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelActive:"+ctx.name());
        super.channelActive(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        log.info("channelInactive:"+ctx.name());
        super.channelInactive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.info("channelRead:"+ctx.name());
        Object result = new Object();
        InvokerProtocol request = (InvokerProtocol)msg;
        if (registryMap.containsKey(request.getClassName())) {
            Object clazz = registryMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getParams());
            result = method.invoke(clazz, request.getValues());
            ctx.write(result);
            ctx.flush();
            ctx.close();
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        log.info("channelReadComplete:"+ctx.name());
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        log.info("userEventTriggered:"+ctx.name());
        super.userEventTriggered(ctx, evt);
    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
        log.info("channelWritabilityChanged:"+ctx.name());
        super.channelWritabilityChanged(ctx);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerAdded:"+ctx.name());
        super.handlerAdded(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        log.info("handlerRemoved:"+ctx.name());
        super.handlerRemoved(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("server exceptionCaught:"+ctx.name());
        cause.printStackTrace();
        ctx.close();
    }

}


























































