package com.cat.common.security;

import lombok.Data;

@Data
public class UserSecurityInfo {
    private String userId;

    private String userName;

    private boolean accountNonLocked;

    private boolean isAuthenticated;
}
