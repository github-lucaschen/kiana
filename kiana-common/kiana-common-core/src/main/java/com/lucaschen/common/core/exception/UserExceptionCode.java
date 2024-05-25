package com.lucaschen.common.core.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UserExceptionCode implements ExceptionCode {
    USER_CAPTCHA_408("USER-CAPTCHA-408", "验证码失效"),
    USER_CAPTCHA_500("USER-CAPTCHA-408", "验证码错误"),
    USER_INFO_500("USER-INFO-500", "用户信息"),
    USER_PASSWORD_500("USER-PASSWORD-500", "用户密码不正确或不符合规范"),
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
