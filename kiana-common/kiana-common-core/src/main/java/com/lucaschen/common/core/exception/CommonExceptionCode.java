package com.lucaschen.common.core.exception;


import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CommonExceptionCode implements ExceptionCode {
    SYSTEM_HTTP_500("SYSTEM-HTTP-500", "未知异常，请联系管理员处理"),
    SYSTEM_HTTP_501("SYSTEM-HTTP-501", "入参错误或入参不全"),
    SYSTEM_HTTP_502("SYSTEM-HTTP-502", "服务调用异常"),
    SYSTEM_HTTP_503("SYSTEM-HTTP-503", "服务调用超时"),

    SYSTEM_HTTP_401("SYSTEM-HTTP-401", "接口未授权，请联系管理员"),
    SYSTEM_HTTP_404("SYSTEM-HTTP-404", "路径不存在，请检查路径是否正确"),
    SYSTEM_HTTP_5000("SYSTEM-HTTP-5000", "请求方式错误"),
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
