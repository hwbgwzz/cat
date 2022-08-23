package com.cat.netty.client;

import com.cat.netty.client.bootstrap.EchoClient;

public class ClientApplication {
    public static void main(String[] args) {
        EchoClient echoClient = new EchoClient("127.0.0.1",17979);
        try {
            echoClient.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
