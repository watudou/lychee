package org.lychee.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * 公共枚举接口
 *
 * @author lizhixiao
 */
public interface BaseEnum {

    static Map hello() {
        return new HashMap();
    }

    /**
     * 枚举key
     *
     * @return
     */
    String getKey();

    /**
     * 枚举value
     *
     * @return
     */
    String getValue();

}
