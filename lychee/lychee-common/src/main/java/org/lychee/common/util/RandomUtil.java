/**
 * Copyright (c) 2011-2014, hata (hatamail@qq.com).
 *
 */
package org.lychee.common.util;

import java.util.Random;

/**
 * <b>模块名称</b><br>
 * 概要说明<br>
 * 
 * @author lizhixiao
 * @date 2018年7月1日
 */
public class RandomUtil {

	public static final String number = "num";
	public static final String letter = "let";
	public static final String mix = "mix";

	/**
	 * 生成随机数字和字母
	 * 
	 * @param length 长度
	 * @param type 类型,RandomUtil属性（数字，字母，混合）
	 * @return 返回
	 */
	public static String getStringRandom(int length, String type) {
		String val = "";
		Random random = new Random();
		// 参数length，表示生成几位随机数
		for (int i = 0; i < length; i++) {
			String charOrNum = type;
			if (RandomUtil.mix.equals(type))
				charOrNum = random.nextInt(2) % 2 == 0 ? "let" : "num";
			// 输出字母还是数字
			if (RandomUtil.letter.equalsIgnoreCase(charOrNum)) {
				// 输出是大写字母还是小写字母
				int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (random.nextInt(26) + temp);
			} else if (RandomUtil.number.equalsIgnoreCase(charOrNum)) {
				val += String.valueOf(random.nextInt(10));
			}
		}
		return val;
	}
}
