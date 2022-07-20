package com.cat.sys.controller;

import com.cat.common.bean.sys.vo.CatUserVO;
import com.cat.sys.service.CatUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final CatUserService catUserService;

    @GetMapping("/getCatUserByUserName")
    public CatUserVO getCatUserByUserName(@RequestParam("userName") String userName) {
        return catUserService.getCatUserByUserName(userName);
    }
}
