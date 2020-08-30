package org.lychee.web.controller;

import org.lychee.constant.LycheeConstant;
import org.springframework.validation.annotation.Validated;

/**
 * controller公共父类
 *
 * @author lizhixiao
 */
@Validated
public class BaseController<T> {


    /**
     * 成功返回
     */
    protected AjaxResult<T> callbackSuccess(T data) {
        return new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, data);
    }

    /**
     * 成功返回,指定code
     */
    protected AjaxResult<T> callbackSuccess(Integer code, T data) {
        return new AjaxResult(code, null, data);
    }

    /**
     * 成功返回
     */
    protected AjaxResult<T> callbackSuccessData(T data) {
        Result result = new Result();
        result.setResult(data);
        return new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, result);
    }

    /**
     * 失败返回
     */
    protected AjaxResult<T> callbackFail(String msg) {
        return new AjaxResult(LycheeConstant.RESPONSE_ERROR_CODE, msg, null);
    }

    /**
     * 失败返回,指定code
     */
    protected AjaxResult<T> callbackFail(Integer code, String msg) {
        return new AjaxResult(code, msg, null);
    }

}
