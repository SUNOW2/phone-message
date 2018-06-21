package com.software.phone.exception;

/**
 * @author 徐旭
 * @data 2018/6/20 13:31
 */
public class MedicalException extends BaseException {
    private static final long serialVersionUID = 1L;

    public MedicalException() {
        super();
    }

    public MedicalException(String msg) {
        super(msg);
    }

    public MedicalException(int errorCode, String msg) {
        super(errorCode, msg);
    }

    public MedicalException(String msg, Throwable throwable) {
        super(msg, throwable);
    }

    public MedicalException(int errorCode, String msg, Throwable throwable) {
        super(errorCode, msg, throwable);
    }
}