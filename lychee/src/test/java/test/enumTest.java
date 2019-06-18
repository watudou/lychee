/**
 * Copyright (c) 2011-2014, hata (hatamail@qq.com).
 */
package test;

import java.util.Map;

import org.lychee.enums.StatusEnum;
import org.lychee.web.util.EnumUtil;

/**
 * <b>模块名称</b><br>
 * 概要说明<br>
 * 
 * @author lizhixiao
 * @date 2018年6月18日
 */
public class enumTest {

	public static void main(String[] args) {
		Class<StatusEnum> clasz = StatusEnum.class;
		Map<Integer, String> map = EnumUtil.EnumToMap(clasz);
		System.out.println("获取枚举的map集合----------：" + map);
		String des = EnumUtil.getValue(1, clasz);
		System.out.println("获取值为1的描述------------：" + des);

		System.out.println("获取  启用的value值-----：" + EnumUtil.getKey("错误返回", clasz));

	}
}
