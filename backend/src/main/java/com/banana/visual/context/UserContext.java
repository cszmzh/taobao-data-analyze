package com.banana.visual.context;

import com.banana.visual.entity.mysql.User;

/**
 * 该类使用线程以便上下文共享用户信息
 */
public class UserContext {

    // 构造函数私有化，外部没有new
    private UserContext() {

    }

    private static final ThreadLocal<User> context = new ThreadLocal<User>();

    /**
     * 存放用户信息
     */
    public static void set(User user) {
        context.set(user);
    }

    /**
     * 获取用户信息
     */
    public static User get() {
        return context.get();
    }

    /**
     * 清除当前进程引用，防止内存泄漏
     */
    public static void remove() {
        context.remove();
    }
}
