package com.lucaschen.common.core.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum AuthExceptionCode implements ExceptionCode {
    AUTH_LOGIN_404("AUTH-LOGIN-404", "未能通过的登录认证"),
    AUTH_PERMISSION_404("AUTH-PERMISSION-404", "未能通过的权限认证"),
    AUTH_ROLE_404("AUTH-ROLE-404", "未能通过的角色认证"),
    AUTH_INNER_404("AUTH-INNER-500", "内部认证失败"),
    AUTH_PREAUTHORIZE_404("AUTH-PREAUTHORIZE-500", "权限认证失败"),
    ;

    private final String code;
    private final String message;

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
