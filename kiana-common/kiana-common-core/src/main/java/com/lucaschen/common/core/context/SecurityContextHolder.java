package com.lucaschen.common.core.context;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.lucaschen.common.core.constant.SecurityConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 获取当前线程变量中的 用户 ID、用户名称、Token 等信息
 * 注意： 必须在网关通过请求头的方法传入，同时在 HeaderInterceptor 拦截器设置值。 否则这里无法获取
 */
public class SecurityContextHolder {
    private static final TransmittableThreadLocal<Map<String, Object>> THREAD_LOCAL =
            new TransmittableThreadLocal<>();

    private SecurityContextHolder() {
    }

    public static Map<String, Object> getLocalMap() {
        Map<String, Object> map = THREAD_LOCAL.get();
        if (map == null) {
            map = new ConcurrentHashMap<>();
            THREAD_LOCAL.set(map);
        }
        return map;
    }

    public static void setLocalMap(final Map<String, Object> threadLocalMap) {
        THREAD_LOCAL.set(threadLocalMap);
    }

    public static void set(final String key, final Object value) {
        final Map<String, Object> map = getLocalMap();
        map.put(key, Objects.isNull(value) ? StringUtils.EMPTY : value);
    }

    public static String get(final String key) {
        final Map<String, Object> map = getLocalMap();
        return map.getOrDefault(key, StringUtils.EMPTY).toString();
    }

    public static Long getUserId() {
        return NumberUtils.toLong(get(SecurityConstants.DETAILS_USER_ID), 0L);
    }

    public static void setUserId(final String account) {
        set(SecurityConstants.DETAILS_USER_ID, account);
    }

    public static String getUserName() {
        return get(SecurityConstants.DETAILS_USERNAME);
    }

    public static void setUserName(final String username) {
        set(SecurityConstants.DETAILS_USERNAME, username);
    }

    public static String getUserKey() {
        return get(SecurityConstants.USER_KEY);
    }

    public static void setUserKey(final String userKey) {
        set(SecurityConstants.USER_KEY, userKey);
    }

    public static String getPermission() {
        return get(SecurityConstants.ROLE_PERMISSION);
    }

    public static void setPermission(final String permissions) {
        set(SecurityConstants.ROLE_PERMISSION, permissions);
    }

    public static void remove() {
        THREAD_LOCAL.remove();
    }
}