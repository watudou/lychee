package org.lychee.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 枚举公共类
 *
 * @author lizhixiao
 */
public class StringEnumUtil {

    private static final String keyMethod = "getKey";
    private static final String valueMethod = "getValue";

    /**
     * 获取key
     */
    public static <T> String getKey(String value, Class<T> enumClass) {
        return StringEnumUtil.getEnumKeyValue(value, enumClass, valueMethod, keyMethod);
    }

    public static <T> String getValue(String key, Class<T> enumClass) {
        return StringEnumUtil.getEnumKeyValue(key, enumClass, keyMethod, valueMethod);
    }

    /**
     * 枚举转map
     */
    public static <T> Map<String, String> enumToMap(Class<T> enumClass) {
        Map<String, String> enummap = new HashMap<>();
        if (!enumClass.isEnum()) {
            return enummap;
        }
        T[] enums = enumClass.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enummap;
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            enummap.put(getMethodValue(keyMethod, tobj), getMethodValue(valueMethod, tobj));
        }
        return enummap;

    }

    /**
     * 根据反射，通过方法名称获取方法值，忽略大小写的
     *
     * @param kvMethod 方法
     * @param obj      枚举
     * @param args
     * @return return value
     */
    private static <T> String getMethodValue(String kvMethod, T obj, Object... args) {
        try {
            /** 获取方法数组，这里只要共有的方法 */
            Method[] methods = obj.getClass().getMethods();
            if (methods.length <= 0) {
                return null;
            }
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                /** 忽略大小写取方法 */
                if (methods[i].getName().equalsIgnoreCase(kvMethod)) {
                    /** 如果存在，则取出正确的方法名称 */
                    method = methods[i];
                    break;
                }
            }
            if (method == null) {
                return null;
            }
            return (String) method.invoke(obj, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过value值获取对应的描述信息
     *
     * @param value
     * @param enumClass
     * @param keyMethod
     * @param valueMethod
     * @return enum description
     */
    private static <T> String getEnumKeyValue(String value, Class<T> enumClass, String keyMethod, String valueMethod) {
        /** 不是枚举则返回"" */
        if (!enumClass.isEnum()) {
            return null;
        }
        /** 获取枚举的所有枚举属性，似乎这几句也没啥用，一般既然用枚举，就一定会添加枚举属性 */
        T[] enums = enumClass.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return null;
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T t = enums[i];
            try {
                String resultValue = getMethodValue(keyMethod, t);
                if (resultValue.equals(value + "")) {
                    /** 存在则返回对应描述 */
                    return getMethodValue(valueMethod, t);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
