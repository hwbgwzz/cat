package com.cat.gateway.route;

import com.alibaba.nacos.api.config.annotation.NacosConfigListener;
import com.cat.common.toolkit.json.JSON;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.event.RefreshRoutesEvent;
import org.springframework.cloud.gateway.route.RouteDefinition;
import org.springframework.cloud.gateway.route.RouteDefinitionWriter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * Nacos实现动态路由
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class NacosDynamicRouteService implements ApplicationEventPublisherAware {

    private final RouteDefinitionWriter routeDefinitionWriter;

    private ApplicationEventPublisher applicationEventPublisher;

    /** 路由id */
    private static List<String> routeIds = Lists.newArrayList();

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    /**
     * 监听nacos路由配置，动态改变路由
     * @param configInfo
     */
    @NacosConfigListener(dataId = "dynamic-routes-config", groupId = "GATEWAY_SERVER_GROUP")
    public void routeConfigListener(String configInfo) {
        clearRoute();
        try {
            List<RouteDefinition> gatewayRouteDefinitions = JSON.toJavaObjectList(configInfo, RouteDefinition.class);
            for (RouteDefinition routeDefinition : gatewayRouteDefinitions) {
                addRoute(routeDefinition);
            }
            publish();
            log.info("Dynamic Routing Publish Success");
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

    }

    /**
     * 清空路由
     */
    private void clearRoute() {
        for (String id : routeIds) {
            routeDefinitionWriter.delete(Mono.just(id)).subscribe();
        }
        routeIds.clear();
    }

    /**
     * 添加路由
     *
     * @param definition
     */
    private void addRoute(RouteDefinition definition) {
        try {
            routeDefinitionWriter.save(Mono.just(definition)).subscribe();
            routeIds.add(definition.getId());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 发布路由、使路由生效
     */
    private void publish() {
        this.applicationEventPublisher.publishEvent(new RefreshRoutesEvent(this.routeDefinitionWriter));
    }
}
