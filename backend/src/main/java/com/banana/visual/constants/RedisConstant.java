package com.banana.visual.constants;

public class RedisConstant {

    // 用户点赞商品记录主键 LIKE_PROD:USER:{用户ID}
    public static String LIKE_PROD_USER_KEY = "LIKE_PROD:USER:";

    // 商品点赞数主键
    public static String LIKE_PROD_NUM_KEY = "LIKE_PROD:NUM";

    // 用户收藏商品记录主键 COLLECT_PROD:USER:{用户ID}
    public static String COLLECT_PROD_USER_KEY = "COLLECT_PROD:USER:";

    // 商品收藏数主键
    public static String COLLECT_PROD_NUM_KEY = "COLLECT_PROD:NUM";

    // 用户搜索商品记录主键
    public static String REC_PRODUCT_SEARCH = "REC_PROD_SEARCH:USER:";

    // 用户游览商品记录逐渐
    public static String REC_PRODUCT_VIEW = "REC_PROD_VIEW:USER:";

    public static String USER_WS_SESSION_KEY = "USER_WS_SESSION_ID";
}
