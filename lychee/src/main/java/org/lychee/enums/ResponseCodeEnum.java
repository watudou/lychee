package org.lychee.enums;

/**
 * 状态枚举
 *
 * @author lizhixiao
 */
public enum ResponseCodeEnum implements IEnum {
    /**
     * 成功返回
     */
    SUCCESS(0, "成功"),
    /**
     * 失败返回
     */
    ERROR(1, "失败"),
    /**
     * 未登录
     */
    NO_LOGIN(401, "未登录"),
    /**
     * 无权限
     */
    NO_AUTH(403, "无权限");

    private final Integer key;
    private final String value;

    private ResponseCodeEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getValue() {
        return value;
    }

}
