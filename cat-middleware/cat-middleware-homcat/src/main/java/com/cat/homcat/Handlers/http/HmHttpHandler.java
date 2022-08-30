package com.cat.homcat.Handlers.http;

import com.cat.homcat.http.request.HmRequest;
import com.cat.homcat.http.response.HmResponse;
import com.cat.homcat.http.servlet.HmServlet;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class HmHttpHandler extends ChannelInboundHandlerAdapter {
    private final Map<String, HmServlet> servletMapping;

    public HmHttpHandler (Map<String, HmServlet> _servletMapping) {
        servletMapping = _servletMapping;
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

        if (msg instanceof HttpRequest) {
            HttpRequest request = (HttpRequest) msg;

            HmRequest hmRequest = new HmRequest(ctx, request);;
            HmResponse hmResponse = new HmResponse(ctx, request);

            String url = request.uri();
            if (servletMapping.containsKey(url)) {
                servletMapping.get(url).service(hmRequest, hmResponse);
            } else {
                hmResponse.write("404 - Not Found");
            }
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
        log.info("exceptionCaught:"+ctx.name());
        cause.printStackTrace();
        ctx.close();
    }
}
