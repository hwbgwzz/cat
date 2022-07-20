package com.cat.common.client.sys;

import com.cat.common.bean.sys.vo.CatUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "cat-service-sys", fallbackFactory = SysClientFallbackFactory.class)
public interface SysClient {

    @GetMapping("/getCatUserByUserName")
    CatUserVO getCatUserByUserName(@RequestParam("userName") String userName);
}
