package org.lychee.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.lychee.constant.LycheeConstant;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * controller公共父类
 *
 * @author lizhixiao
 */
public class SupperController {

    @Autowired
    protected HttpServletRequest request;
    @Autowired
    protected HttpServletResponse response;

    /**
     * 成功返回
     * @param data
     * @return josnStr
     */
    protected String callbackSuccess(Object data) {
        return JSON.toJSONString(new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, data), SerializerFeature.WriteMapNullValue);
    }

    /**
     * 成功返回
     * @param data
     * @return data josnStr
     */
    protected String callbackSuccessData(Object data) {
        Result result = new Result();
        result.setResult(data);
        String callback = JSON.toJSONString(new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, result));
        return callback;
    }

    /**
     * @param msg 失败信息
     * 失败返回
     */
    protected String callbackFail(String msg) {
        String callback = JSON.toJSONString(new AjaxResult(LycheeConstant.RESPONSE_ERROR_CODE, msg, null));
        return callback;
    }

    /**
     *  @param code 失败信息code
     *  @param msg 失败信息
     * 失败返回,指定code
     */
    protected String callbackFail(Integer code, String msg) {
        String callback = JSON.toJSONString(new AjaxResult(code, msg, null));
        return callback;
    }

}
