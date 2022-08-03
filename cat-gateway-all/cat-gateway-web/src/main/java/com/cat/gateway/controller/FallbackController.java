package com.cat.gateway.controller;

import com.cat.common.bean.Result;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingClass;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关调用微服务熔断降級处理
 */
@RestController
@RequestMapping("/fallback")
@ConditionalOnMissingClass
public class FallbackController {

    @RequestMapping("")
    public Result<?> fallback(){
        return Result.error("服务暂不可用");
    }

}
