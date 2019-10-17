package org.lychee.web.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.lychee.constant.LycheeConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;

/**
 * controller公共父类
 *
 * @author lizhixiao
 */
public class BaseController {

    @Autowired
    protected ServerHttpRequest request;
    @Autowired
    protected ServerHttpResponse response;

    /**
     * 成功返回
     */
    protected String callbackSuccess(Object data) {
        return JSON.toJSONString(new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, data), SerializerFeature.WriteMapNullValue);
    }

    /**
     * 成功返回
     */
    protected String callbackSuccessData(Object data) {
        Result result = new Result();
        result.setResult(data);
        String callback = JSON.toJSONString(new AjaxResult(LycheeConstant.RESPONSE_SUCCESS_CODE, null, result));
        return callback;
    }

    /**
     * 失败返回
     */
    protected String callbackFail(String msg) {
        String callback = JSON.toJSONString(new AjaxResult(LycheeConstant.RESPONSE_ERROR_CODE, msg, null));
        return callback;
    }

    /**
     * 失败返回,指定code
     */
    protected String callbackFail(Integer code, String msg) {
        String callback = JSON.toJSONString(new AjaxResult(code, msg, null));
        return callback;
    }

}
