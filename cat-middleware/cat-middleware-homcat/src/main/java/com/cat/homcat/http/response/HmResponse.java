package com.cat.homcat.http.response;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.*;
import io.netty.util.CharsetUtil;

public class HmResponse {
    private ChannelHandlerContext ctx;

    private HttpRequest request;

    public HmResponse(ChannelHandlerContext _ctx, HttpRequest _request) {
        this.ctx = _ctx;
        this.request = _request;
    }

    public void write(String data) throws Exception {
        try {
             if (data == null || data.length() == 0) {
                 return;
             }
            FullHttpResponse response = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1,
                    HttpResponseStatus.OK,
                    Unpooled.wrappedBuffer(data.getBytes(CharsetUtil.UTF_8)));
             response.headers().set("Content-Type", "text/html");
             ctx.write(response);
        } finally {
            ctx.flush();
            ctx.close();
        }
    }
}




















