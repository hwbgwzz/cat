package com.cat.gateway.controller;

import com.cat.common.bean.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 网关调用服务熔断处理
 */
@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @RequestMapping("")
    public Result<?> fallback(){
        return Result.error("服务暂不可用！！！！");
    }

}
