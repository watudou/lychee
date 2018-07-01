/**
 * Copyright (c) 2011-2014, hata (hatamail@qq.com).
 *
 */
package org.lychee.web.util;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;

/**
 * <b>Token工具类</b><br>
 * 生成验证token<br>
 * 
 * @author lizhixiao
 * @date 2018年6月18日
 */
public class TokenUtil {
	private static String randomId;
	static {
		randomId = UUID.randomUUID().toString().replace("-", "");
	}

	/**
	 * 概要说明<br>
	 * 
	 * @param 参数
	 * @return 返回
	 */
	private void createToken(HttpServletRequest request) {
		String ip = getIp(request);
		long time = System.currentTimeMillis();
	}

	/**
	 * 概要说明<br>
	 * 
	 * @param 参数
	 * @return 返回
	 */
	private void validateToken() {
		// TODO Auto-generated method stub

	}

	static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}
