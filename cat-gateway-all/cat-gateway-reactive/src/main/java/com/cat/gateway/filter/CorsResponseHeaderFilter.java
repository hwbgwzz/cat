package com.cat.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.NettyWriteResponseFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 跨域全局过滤器处理去重Vary 和 Access-Control-Allow-Origin
 * Vary 和 Access-Control-Allow-Origin 两个头重复了两次，其中浏览器对后者有唯一性限制
 */
@Slf4j
@Component
public class CorsResponseHeaderFilter implements GlobalFilter, Ordered {

    private static final String ANY = "*";

    @Override
    public int getOrder() {
        // 指定此过滤器位于NettyWriteResponseFilter之后
        // 即待处理完响应体后接着处理响应头
        return NettyWriteResponseFilter.WRITE_RESPONSE_FILTER_ORDER + 1;
    }

    @Override
    @SuppressWarnings("serial")
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            exchange.getResponse().getHeaders().entrySet().stream()
                    .filter(kv -> (kv.getValue() != null && kv.getValue().size() > 1))
                    .filter(kv -> (kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN)
                            || kv.getKey().equals(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS)
                            || kv.getKey().equals(HttpHeaders.VARY)))
                    .forEach(kv ->
                    {
                        // Vary只需要去重即可
                        if(kv.getKey().equals(HttpHeaders.VARY))
                            kv.setValue(kv.getValue().stream().distinct().collect(Collectors.toList()));
                        else{
                            List<String> value = new ArrayList<>();
                            if(kv.getValue().contains(ANY)){  //如果包含*，则取*
                                value.add(ANY);
                                kv.setValue(value);
                            }else{
                                value.add(kv.getValue().get(0)); // 否则默认取第一个
                                kv.setValue(value);
                            }
                        }
                    });
        }));
    }
}
