package com.lucaschen.common.core.domain;

import com.lucaschen.common.core.constant.Constants;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class R<T> {
    /**
     * 成功
     */
    public static final int SUCCESS_DEFAULT_CODE = Constants.SUCCESS;

    /**
     * 失败
     */
    public static final int FAILURE_DEFAULT_CODE = Constants.FAIL;

    /**
     * 请求数据
     */
    private T data;

    /**
     * 返回消息
     */
    private String message;

    /**
     * 返回码
     */
    private int code;

    public static <T> R<T> ok() {
        return result(null, SUCCESS_DEFAULT_CODE, null);
    }

    public static <T> R<T> ok(final T data) {
        return result(data, SUCCESS_DEFAULT_CODE, null);
    }

    public static <T> R<T> ok(final T data, final String message) {
        return result(data, SUCCESS_DEFAULT_CODE, message);
    }

    public static <T> R<T> fail() {
        return result(null, FAILURE_DEFAULT_CODE, null);
    }

    public static <T> R<T> fail(final String message) {
        return result(null, FAILURE_DEFAULT_CODE, message);
    }

    public static <T> R<T> fail(final T data) {
        return result(data, FAILURE_DEFAULT_CODE, null);
    }

    public static <T> R<T> fail(final T data, final String message) {
        return result(data, FAILURE_DEFAULT_CODE, message);
    }

    public static <T> R<T> fail(final int code, final String message) {
        return result(null, code, message);
    }

    public static <T> Boolean isSuccess(R<T> result) {
        return R.SUCCESS_DEFAULT_CODE == result.getCode();
    }

    public static <T> Boolean isError(R<T> result) {
        return !isSuccess(result);
    }

    private static <T> R<T> result(final T data, final int code, final String message) {
        final R<T> result = new R<>();
        result.setData(data);
        result.setMessage(message);
        result.setCode(code);
        return result;
    }
}
