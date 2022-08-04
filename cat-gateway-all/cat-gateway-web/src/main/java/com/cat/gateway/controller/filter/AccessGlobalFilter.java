package com.cat.gateway.controller.filter;

import com.cat.common.constant.PathConstant;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * 访问全局过虑器
 * 对所有路由进行访问控制
 */
@Component
public class AccessGlobalFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        boolean formLoginFlag = isItFormLoginRequest(exchange);
        if (formLoginFlag) {
            chain.filter(exchange);
        } else {
            headerTokenHandler(exchange, chain);
        }
        return Mono.empty();
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    /**
     * 是不是 form 表单登录的请求
     * true: 继续执行, security认证
     * true: 继续执行, security认证
     * false: token认证
     * @param exchange
     * @return
     */
    private boolean isItFormLoginRequest(final ServerWebExchange exchange) {
        boolean result = false;
        String requestUriPath = exchange.getRequest().getURI().getPath();
        if (requestUriPath.startsWith(PathConstant.LOGIN_PATH)) {
            result = true;
        }
        return result;
    }

    private void headerTokenHandler(final ServerWebExchange exchange, final GatewayFilterChain chain) {
            boolean checkFlag = checkHeaderTokenInfo(exchange);
            if (checkFlag) {
                transferTokenToMicroServices(exchange, chain);
            } else {
                tokenInvalidHandler(exchange);
            }
    }

    /**
     * 检查请求头中的token信息
     * token合法： 继续执行访问微服务资源，并且把token传递到微服务
     * token不合法: 响应客户端token验证失败: 具体原因（token has null, token has fiddle， token has expires, token has parsed fail）
     * @param exchange
     */
    private boolean checkHeaderTokenInfo(final ServerWebExchange exchange) {
        boolean result = false;

        return result;
    }

    /**
     *  transmit the token to micro-services
     * @param exchange
     * @param chain
     */
    private void transferTokenToMicroServices(final ServerWebExchange exchange, final GatewayFilterChain chain){

        chain.filter(exchange);
    }

    /**
     * token is invalid
     * @param exchange
     */
    private void tokenInvalidHandler(final ServerWebExchange exchange) {

    }
}
