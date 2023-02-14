package com.banana.visual.enums;

import lombok.Getter;

@Getter
public enum ResultEnum {
    /**
     * 解密Token公共信息出现JWTDecodeException异常
     */
    JWTTOEN_JWTDECODE(50000, "解密Token公共信息出现JWTDecodeException异常"),
    /**
     * JWTToken认证解密出现UnsupportedEncodingException异常
     */
    JWTTOEN_UNSUPPORTEDENCODING(50000, "JWTToken认证解密出现UnsupportedEncodingException异常"),
    /**
     * 参数有误
     */
    ERROR_IN_PARAM(50001, "参数有误"),
    /**
     * 未请求到数据
     */
    DATA_NOT_REQUESTED(50002, "数据获取异常,请核查"),

    /**
     * 数据插入异常
     */
    DATA_INSERT_EXCEPTION(50003, "数据插入异常"),
    /**
     * 数据更新异常
     */
    DATA_UPDATE_EXCEPTION(50004, "数据更新异常"),
    /**
     * 数据删除异常
     */
    DATA_DELETION_EXCEPTION(50005, "数据删除异常"),
    /**
     * 用户不存在
     */
    USER_NOT_EXIST(50006, "用户不存在"),
    /**
     * 用户密码错误
     */
    USER_PASSWORD_ERROR(50006, "用户密码错误"),
    /**
     * 用户状态错误
     */
    USER_STATUS_ERROR(50006, "用户状态错误"),
    /**
     * 服务器异常
     */
    SERVER_ERROR(50007, "服务器异常"),
    /**
     * 参数格式错误
     */
    REG_ERROR(50008, "参数格式错误"),
    /**
     * 昵称已存在错误
     */
    NICKNAME_EXIST_ERROR(50009, "昵称已存在"),
    /**
     * 昵称在指定期限内已修改错误
     */
    NICKNAME_TIME_INTERVAL_ERROR(50010, "昵称在指定期限内已修改"),
    /**
     * 根据UID找不到用户
     */
    UID_NOT_FOUND(60000, "用户UID不存在，请重新输入"),
    /**
     * 岗位编号已存在
     */
    JID_IS_EXIST(60010, "岗位编号已存在，请重新输入");

    private Integer code;

    private String message;

    ResultEnum(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}