package org.lychee.web.request;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * @author lizhixiao
 * @date: 2018年2月9日
 * @Description:解析request头 json参数还原原始类型
 */
public class RequestParameterWrapper extends HttpServletRequestWrapper {

	private Map<String, String[]> params = new HashMap<String, String[]>();

	public RequestParameterWrapper(HttpServletRequest request) {
		super(request);
		// 将现有parameter传递给params
		this.params.putAll(request.getParameterMap());
	}

	// 重载构造函数
	public RequestParameterWrapper(HttpServletRequest request, Map<String, Object> extraParams) {
		super(request);
		addParameters(extraParams);
	}

	// 添加额外参数
	public void addParameters(Map<String, Object> extraParams) {
		for (Map.Entry<String, Object> entry : extraParams.entrySet()) {
			addParameter(entry.getKey(), entry.getValue());
		}
	}

	public void addParameter(String name, Object value) {
		if (value != null) {
			if (value instanceof String[]) {
				params.put(name, (String[]) value);
			} else if (value instanceof String) {
				params.put(name, new String[] { (String) value });
			} else {
				params.put(name, new String[] { String.valueOf(value) });
			}
		}
	}

	public void parseJson(HttpServletRequest request) {
		// String requestMsg = getRequestMsg(request);
		// JSONObject requestJSON = JSONObject.parseObject(requestMsg);

	}

	public String getRequestMsg(HttpServletRequest request) throws IOException {
		BufferedReader reader = request.getReader();
		String tempStr = null;
		StringBuffer sb = new StringBuffer();
		while ((tempStr = reader.readLine()) != null) {
			sb.append(tempStr);
		}
		return sb.toString();
	}
}