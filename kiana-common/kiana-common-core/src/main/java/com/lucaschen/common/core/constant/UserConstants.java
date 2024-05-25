package com.lucaschen.common.core.constant;

/**
 * 用户常量信息
 */
public final class UserConstants {
    /**
     * 菜单类型（目录）
     */
    public static final String TYPE_DIR = "D";
    /**
     * 菜单类型（菜单）
     */
    public static final String TYPE_MENU = "M";
    /**
     * 菜单类型（按钮）
     */
    public static final String TYPE_BUTTON = "B";
    /**
     * 用户名长度限制
     */
    public static final int USERNAME_MIN_LENGTH = 2;
    public static final int USERNAME_MAX_LENGTH = 20;
    /**
     * 密码长度限制
     */
    public static final int PASSWORD_MIN_LENGTH = 5;
    public static final int PASSWORD_MAX_LENGTH = 20;

    private UserConstants() {
    }
}
