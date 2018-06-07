package com.software.phone.controller;

import com.software.phone.conf.ResponseEntity;

import java.util.Collections;

public class BaseController {

    /**
     * 成功的Status Code
     */
    private static final int RESCODE_OK = 200;

    /**
     * 失败的Status Code
     */
    private static final int RESCODE_FAIL = 201;

    /**
     * 描述：获取成功信息
     * @return
     */
    protected ResponseEntity getSuccessResult(String msg) {
        return new ResponseEntity("ok", RESCODE_OK, msg, Collections.EMPTY_MAP);
    }

    /**
     * 描述：获取默认ajax成功信息
     * @return
     */
    protected ResponseEntity getSuccessResult() {
        return getSuccessResult("操作成功");
    }

    /**
     * 描述：获取成功结果
     * @param obj
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> getSuccessResult(T obj) {
        return new ResponseEntity<>("ok", RESCODE_OK, "操作成功", obj);
    }

    /**
     * 描述：获取成功信息和结果
     * @param msg
     * @param obj
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> getSuccessResult(String msg, T obj) {
        return new ResponseEntity<>("ok", RESCODE_OK, "操作成功", obj);
    }


    /**
     * 描述：获取失败信息
     * @param msg
     * @return
     */
    protected ResponseEntity getFailResult(String msg) {
        return new ResponseEntity("fail", RESCODE_FAIL, "操作失败", Collections.EMPTY_MAP);
    }

    /**
     * 描述：获取默认ajax失败信息
     * @return
     */
    protected ResponseEntity getFailResult() {
        return getFailResult("操作失败");
    }

    /**
     * 描述：获取失败结果
     * @param obj
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> getFailResult(T obj) {
        return new ResponseEntity<>("fail", RESCODE_FAIL, "操作失败", obj);
    }

    /**
     * 描述：获取失败信息和结果
     * @param msg
     * @param errorCode
     * @param obj
     * @param <T>
     * @return
     */
    protected <T> ResponseEntity<T> getFailResult(String msg, int errorCode,T obj) {
        return new ResponseEntity<>("fail", errorCode, msg, obj);
    }
}
