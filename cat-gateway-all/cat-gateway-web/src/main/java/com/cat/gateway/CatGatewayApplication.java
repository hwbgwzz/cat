package com.cat.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.cat", exclude= ReactiveSecurityAutoConfiguration.class)
@EnableDiscoveryClient
@EnableFeignClients
@EnableHystrix
public class CatGatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(CatGatewayApplication.class, args);
    }
}
