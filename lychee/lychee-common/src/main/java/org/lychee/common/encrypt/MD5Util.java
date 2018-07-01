/**
 * Copyright (c) 2011-2014, hata (hatamail@qq.com).
 *
 */
package org.lychee.common.encrypt;

import org.apache.commons.codec.digest.Md5Crypt;

/**
 * <b>MD5加密工具类</b><br>
 * 概要说明<br>
 * 
 * @author lizhixiao
 * @date 2018年6月18日
 */
public class MD5Util {
	public static void main(String[] args) {
	}

	public static String encode(String str) {
		return Md5Crypt.md5Crypt(str.getBytes());

	}

	public static String encode(String str, String salt) {
		return Md5Crypt.md5Crypt(mixed(str, salt).getBytes());

	}

	private static String mixed(String str, String salt) {
		if (null == salt)
			return str;
		StringBuffer sb = new StringBuffer();
		int len = str.length();
		if (salt.length() < len) {
			len = salt.length();
		}
		for (int i = 0; i < len; i++) {
			sb.append(str.charAt(i)).append(salt.charAt(i));
		}
		if (salt.length() != str.length()) {
			sb.append(salt.length() > len ? salt.substring(len, salt.length()) : str.substring(len, str.length()));
		}
		return sb.toString();
	}
}
