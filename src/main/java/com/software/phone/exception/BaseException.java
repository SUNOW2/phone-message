package com.software.phone.exception;

import java.io.Serializable;

/**
 * @author 徐旭
 * @data 2018/6/20 13:50
 */
public class BaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_ERROR_CODE = 201;

    /**
     * 错误码：
     */
    private int errorCode;

    public BaseException() {
        super();
    }

    public BaseException(String msg) {
        super(msg);
    }

    public BaseException(int errorCode, String msg) {
        super(msg);
        this.errorCode = errorCode;
    }

    public BaseException(String msg, Throwable throwable) {
        super(msg, throwable);
        this.errorCode = DEFAULT_ERROR_CODE;
    }

    public BaseException(int errorCode, String msg, Throwable throwable) {
        super(msg, throwable);
        this.errorCode = errorCode;
    }
}
