package com.cat.netty.server;

import com.cat.netty.server.bootstrap.EchoServer;

public class ServerApplication {
    public static void main(String[] args) {
        EchoServer echoServer = new EchoServer(17979);
        try {
            echoServer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
