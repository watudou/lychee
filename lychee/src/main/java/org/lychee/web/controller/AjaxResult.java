package org.lychee.web.controller;

import lombok.Data;

/**
 * 响应结果
 *
 * @author lizhixiao
 * @desc controller响应结果
 */
@Data
public class AjaxResult<T> {

    /**
     * 响应code
     */
    private Integer code;
    /**
     * 响应消息
     */
    private String msg;
    /**
     * 响应数据
     */
    private T data;

    public AjaxResult() {
        super();
    }

    public AjaxResult(Integer code) {
        super();
        this.code = code;
    }

    public AjaxResult(Integer code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public AjaxResult(Integer code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
