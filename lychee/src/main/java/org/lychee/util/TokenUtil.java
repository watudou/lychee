/**
 * Copyright (c) 2011-2014, hata (hatamail@qq.com).
 */
package org.lychee.util;


import java.util.UUID;

/**
 * <b>Token工具类</b><br>
 * 生成验证token<br>
 *
 * @author lizhixiao
 * @date 2019年6月18日
 */
public class TokenUtil {

    /**
     * 概要说明<br>
     *
     * @return 返回
     */
    public static String createToken() {
        String randomId = UUID.randomUUID().toString().replace("-", "");
        return randomId;
    }

    /**
     * 概要说明<br>
     *
     * @return 返回
     */
    private void validateToken() {
        // TODO Auto-generated method stub

    }

}
