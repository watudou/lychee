package org.lychee.web.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.plugins.Page;

public class BaseController {

	@Autowired
	protected HttpServletRequest request;
	@Autowired
	protected HttpServletResponse response;

	/**
	 * <p>
	 * 获取分页对象
	 * </p>
	 */
	protected <T> Page<T> getPage() {
		return getPage(10);
	}

	/**
	 * <p>
	 * 获取分页对象
	 * </p>
	 *
	 * @param size 每页显示数量
	 * @return
	 */
	protected <T> Page<T> getPage(int size) {
		int tempSize = size, tempIndex = 1;
		String requestIndex = request.getParameter("_index");
		String requestSize = request.getParameter("_size");
		if (NumberUtils.isDigits(requestIndex)) {
			tempIndex = Integer.parseInt(requestIndex);
		}
		if (NumberUtils.isDigits(requestSize)) {
			tempSize = Integer.parseInt(requestSize);
		}
		if (tempIndex < 1) {
			tempIndex = 1;
		}
		if (tempSize > 100) {
			tempSize = 100;
		}
		return new Page<T>(tempIndex, tempSize);
	}

	/**
	 * 成功返回
	 */
	protected AjaxResult<Object> callbackSuccess(Object data) {
		return new AjaxResult<Object>(HttpServletResponse.SC_OK, null, data);
	}

	/**
	 * 成功返回
	 */
	protected AjaxResult<Object> callbackSuccessData(Object data) {
		Result result = new Result();
		result.setResult(data);
		return new AjaxResult<Object>(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED, null, result);
	}

	/**
	 * 失败返回
	 */
	protected AjaxResult<Object> callbackFail(String msg) {
		return new AjaxResult<Object>(HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED, msg, null);
	}

	/**
	 * 失败返回,自定义code
	 */
	protected AjaxResult<Object> callbackFail(Integer code, String msg) {
		return new AjaxResult<Object>(code, msg, null);
	}

}
