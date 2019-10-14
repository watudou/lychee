package org.lychee.enums;

/**
 * 状态枚举
 *
 * @author lizhixiao
 */
public enum StatusEnum implements IEnum {
    /**
     * 状态启用
     */
    DISABLE(0, "禁用"),
    /**
     * 状态禁用
     */
    ENABLE(1, "启用");

    private final Integer key;
    private final String value;

    private StatusEnum(Integer key, String value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public Integer key() {
        return key;
    }

    @Override
    public String value() {
        return value;
    }

}
