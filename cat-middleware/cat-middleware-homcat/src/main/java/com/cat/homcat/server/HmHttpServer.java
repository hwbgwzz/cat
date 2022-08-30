package com.cat.homcat.server;

import com.cat.homcat.Handlers.http.HmHttpHandler;
import com.cat.homcat.http.servlet.HmServlet;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import lombok.extern.slf4j.Slf4j;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
public class HmHttpServer {
    private int port = 18080;

    private Map<String, HmServlet> servletMapping = new HashMap<String, HmServlet>();

    private Properties webProp = new Properties();

    private HmHttpServer() {

    }

    private HmHttpServer(int _port) {
        port = _port;
    }

    private void init() {
        //load web.properties file, at the same time initialization servletMapping object
        try {
            String WEB_INF = this.getClass().getResource("/").getPath();
            log.info("web_info absolute path is {}",WEB_INF);
            FileInputStream fis = new FileInputStream(WEB_INF + "web.properties");

            webProp.load(fis);

            for (Object value : webProp.keySet()) {
                String key = value.toString();
                if (key.endsWith(".url")) {
                    String servletPrefixName = key.replaceAll("\\.url$","");
                    String url = webProp.getProperty(key);
                    String className = webProp.getProperty(servletPrefixName + ".handlerClass");
                    HmServlet hmServlet = (HmServlet) Class.forName(className).newInstance();
                    servletMapping.put(url,hmServlet);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        }
    }

    private void start() {

        init();

        //Boss Thread
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        //Work Thread
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {

            //Boot configuration
            ServerBootstrap serverBootstrap = new ServerBootstrap();

            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel client) throws Exception {
                            client.pipeline().addLast(new HttpResponseEncoder());
                            client.pipeline().addLast(new HttpRequestDecoder());
                            client.pipeline().addLast(new HmHttpHandler(servletMapping));
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            //start server
            ChannelFuture future = serverBootstrap.bind(port).sync();
            log.info("Homcat is started. The listening port is {}",port);
            future.channel().closeFuture().sync();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            e.printStackTrace();
        } finally {
            try {
                bossGroup.shutdownGracefully().sync();
                workGroup.shutdownGracefully().sync();
            } catch (InterruptedException ie) {
                log.error(ie.getMessage(), ie);
                ie.printStackTrace();
            }
        }
    }

    public static HmHttpServer launch() {
       HmHttpServer server = new HmHttpServer();
       server.start();
       return server;
    }

    public static HmHttpServer launch(int _port) {
        HmHttpServer server = new HmHttpServer(_port);
        server.start();
        return server;
    }
}









































































