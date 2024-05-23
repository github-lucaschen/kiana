package com.lucaschen.common.core.exception;

/**
 * 错误接口
 */
public interface ExceptionCode {

    /**
     * 获取错误编码
     *
     * @return ErrorCode
     */
    String getCode();

    /**
     * 获取错误消息
     *
     * @return ErrorMessage
     */
    String getMessage();
}