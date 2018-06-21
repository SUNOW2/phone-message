package com.software.phone.utils;

/**
 * @author 徐旭
 * @data 2018/6/20 10:55
 */
public interface AppConstantsUtil {
    /**
     * token私有秘钥
     */
    String JWT_SECURITY = "medical";

    public static final int NUM24 = 24;

    public static final int NUM30 = 30;

    public static final int NUM60 = 60;


    interface ExpTime {
        /**
         * 用户登录的有效时间
         */
        int loginExpTime = 10 * 24 * 60 * 60 * 1000;
    }
}