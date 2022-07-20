package com.cat.common.bean.sys.vo;

import lombok.Data;

@Data
public class CatUserVO {
    private String userId;

    private String userName;

    private String password;

    private boolean accountNonLocked;
}
