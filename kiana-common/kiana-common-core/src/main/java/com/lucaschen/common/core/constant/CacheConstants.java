package com.lucaschen.common.core.constant;

/**
 * 缓存常量信息
 */
public final class CacheConstants {
    /**
     * 缓存有效期，默认 720（分钟）
     */
    public static final long EXPIRATION = 720;

    /**
     * 缓存刷新时间，默认 120（分钟）
     */
    public static final long REFRESH_TIME = 120;

    /**
     * 密码最大错误次数
     */
    public static final int PASSWORD_MAX_RETRY_COUNT = 5;

    /**
     * 密码锁定时间，默认 10（分钟）
     */
    public static final long PASSWORD_LOCK_TIME = 10;

    /**
     * 权限缓存前缀
     */
    public static final String LOGIN_TOKEN_KEY = "login:token:";

    /**
     * 登录账户密码错误次数 Redis Key
     */
    public static final String LOGIN_PWD_ERR_CNT_KEY = "login:pwd_err_cnt:";

    /**
     * 验证码 Redis Key
     */
    public static final String LOGIN_CAPTCHA_CODE_KEY = "login:captcha_code:";

    /**
     * 参数管理 Cache Key
     */
    public static final String SYS_CONFIG_KEY = "sys:config:";

    /**
     * 字典管理 Cache Key
     */
    public static final String SYS_DICTIONARY_KEY = "sys:dictionary:";

    /**
     * 登录 IP 黑名单 Cache Key
     */
    public static final String LOGIN_BLACK_IP_LIST = SYS_CONFIG_KEY + "login:blackIPList";

    private CacheConstants() {
    }
}