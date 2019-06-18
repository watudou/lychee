package org.lychee.enums;

public enum StatusEnum implements IEnum {
	DISABLE(0, "禁用"),
	ENABLE(1, "启用");

	private final Integer key;
	private final String value;

	private StatusEnum(Integer key, String value) {
		this.key = key;
		this.value = value;
	}

	public Integer key() {
		return key;
	}

	public String value() {
		return value;
	}

}
