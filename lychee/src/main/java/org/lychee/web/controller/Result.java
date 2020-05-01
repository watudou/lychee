package org.lychee.web.controller;

import lombok.Data;

/**
 * 返回单个参数
 *
 * @author lizhixiao
 */
@Data
public class Result<T> {

    /**
     * 返回结果
     */
    private T result;
}
