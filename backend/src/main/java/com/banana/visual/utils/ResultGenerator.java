package com.banana.visual.utils;

import com.banana.visual.VO.ResultVO;
import com.banana.visual.enums.ResultEnum;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {

    //默认成功返回msg
    private static final String DEFAULT_SUCCESS_MESSAGE = "SUCCESS";
    //默认成功返回code
    private static final Integer DEFAULT_SUCCESS_CODE = 200;
    //默认失败返回code
    private static final Integer DEFAULT_FAIL_CODE = 999999;

    /**
     * 成功,无返回值
     *
     * @return
     */
    public static ResultVO genSuccessResult() {
        return new ResultVO(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    /**
     * 成功,有返回数据
     *
     * @param data
     * @return
     */
    public static ResultVO genSuccessResult(Object data) {
        return new ResultVO(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data);

    }

    /**
     * 失败,返回默认code
     *
     * @param message
     * @return
     */
    public static ResultVO genFailResult(String message) {
        return new ResultVO(DEFAULT_FAIL_CODE, message, null);
    }

    /**
     * 失败,返回自定义code
     *
     * @param code
     * @param message
     * @return
     */
    public static ResultVO genFailResult(int code, String message) {
        return new ResultVO(code, message, null);
    }

    /**
     * 失败,返回自定义code
     *
     * @param resultEnum
     * @return
     */
    public static ResultVO genFailResult(ResultEnum resultEnum) {
        return new ResultVO(resultEnum.getCode(), resultEnum.getMessage(), null);
    }
}
