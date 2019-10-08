package org.lychee.enums;

/**
 * 状态枚举
 *
 * @author lizhixiao
 */
public enum StatusEnum implements IEnum {
    DISABLE(0, "禁用"),
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
