package org.lychee.web.controller;

/**
 * @author lizhixiao
 * @desc controller返回结果
 */
public class AjaxResult<T> {

	/**
	 * 返回codeֵ
	 */
	private Integer code;
	/**
	 * 错误消息
	 */
	private String msg;
	/**
	 * 返回数据
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

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
