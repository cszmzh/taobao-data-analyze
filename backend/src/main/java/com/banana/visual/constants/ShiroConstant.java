package com.banana.visual.constants;

public class ShiroConstant {
    /**
     * redis-key-前缀-shiro:cache:
     */
    public final static String PREFIX_SHIRO_CACHE = "shiro:cache:";

    /**
     * redis-key-前缀-shiro:access_token:
     */
    public final static String PREFIX_SHIRO_ACCESS_TOKEN = "shiro:access_token:";

    /**
     * redis-key-前缀-shiro:refresh_token:
     */
    public final static String PREFIX_SHIRO_REFRESH_TOKEN = "shiro:refresh_token:";

    /**
     * redis-key-前缀-shiro:user:
     */
    public final static String PREFIX_SHIRO_USER = "shiro:user:";

    /**
     * JWT-uid:
     */
    public final static String UID = "uid";

    /**
     * JWT-permission
     */
    public final static String PERM = "permission";

    /**
     * 代表服务渠道 1-PC端  2-移动端
     */
    public final static String SERVICETYPE = "serviceType";

    /**
     * 登陆用户信息/
     */
    public final static String PREFIX_LOGIN_USER = "login:user:";

    /**
     * 根据uid获取用户信息/
     */
    public final static String USER_INFO = "user:info:";

    /**
     * JWT-currentTimeMillis:
     */
    public final static String CURRENT_TIME_MILLIS = "currentTimeMillis";

    /**
     * PASSWORD_MAX_LEN
     */
    public static final Integer PASSWORD_MAX_LEN = 15;
}
