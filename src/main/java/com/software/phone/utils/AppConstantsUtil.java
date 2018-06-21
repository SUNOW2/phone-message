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

    int NUM24 = 24;

    int NUM30 = 30;

    int NUM60 = 60;

    int ERR_NUM = 2;


    interface ExpTime {
        /**
         * 用户登录的有效时间
         */
        int loginExpTime = 10 * 24 * 60 * 60 * 1000;
    }
}