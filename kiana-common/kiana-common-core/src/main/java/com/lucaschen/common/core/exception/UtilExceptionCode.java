package com.lucaschen.common.core.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum UtilExceptionCode implements ExceptionCode {
    UTIL_JSON_001("UTIL-JSON-001", "数据转换异常"),
    UTIL_SERVLET_401("UTIL-SERVLET-401", "获取 Request 失败"),
    UTIL_SERVLET_402("UTIL-SERVLET-401", "获取 RequestAttributes 失败"),
    UTIL_SERVLET_501("UTIL-SERVLET-501", "获取 Response 失败"),
    UTIL_SERVLET_502("UTIL-SERVLET-502", "将字符串渲染到客户端失败"),
    UTIL_UUID_001("UTIL-UUID-502", "生成器生成错误"),
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
