package com.lucaschen.common.core.exception;

import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {
    private final ExceptionCode exceptionCode;

    public GlobalException(final ExceptionCode exceptionCode) {
        this.exceptionCode = exceptionCode;
    }

    public GlobalException(final String code, final String message) {
        this.exceptionCode = new ExceptionCode() {
            @Override
            public String getCode() {
                return code;
            }

            @Override
            public String getMessage() {
                return message;
            }
        };
    }
}