package com.banana.visual.utils;

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64ConvertUtil {
    private static final String DEFAULT_CHARSET_NAME = "utf-8";

    /**
     * 加密JDK1.8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String encode(String str) throws UnsupportedEncodingException {
        byte[] encodeBytes = Base64.getEncoder().encode(str.getBytes(DEFAULT_CHARSET_NAME));
        return new String(encodeBytes);
    }

    /**
     * 解密JDK1.8
     *
     * @param str
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String decode(String str) throws UnsupportedEncodingException {
        byte[] decodeBytes = Base64.getDecoder().decode(str.getBytes(DEFAULT_CHARSET_NAME));
        return new String(decodeBytes);
    }
}
