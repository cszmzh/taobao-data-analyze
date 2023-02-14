package com.banana.visual.utils;

import com.alibaba.fastjson.JSONObject;

public class JsonConvertUtil {

    /**
     * JSON 转 Object
     *
     * @param pojo
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T jsonToObject(String pojo, Class<T> clazz) {
        return JSONObject.parseObject(pojo, clazz);
    }

    /**
     * Object 转 JSON
     *
     * @param t
     * @param <T>
     * @return
     */
    public static <T> String objectToJson(T t) {
        return JSONObject.toJSONString(t);
    }
}