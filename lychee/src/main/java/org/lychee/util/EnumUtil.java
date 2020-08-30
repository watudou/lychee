package org.lychee.util;

import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * enum工具类
 *
 * @author lizhixiao
 */
public class EnumUtil {

    private static final String keyMethod = "getKey";
    private static final String valueMethod = "getValue";

    /**
     * 获取key
     */
    public static <T> Integer getKey(String value, Class<T> enumClass) {
        Integer key = (Integer) EnumUtil.getEnumKeyValue(value, enumClass, valueMethod, keyMethod);
        return key;
    }

    public static <T> String getValue(Integer key, Class<T> enumClass) {
        String value = (String) EnumUtil.getEnumKeyValue(key, enumClass);
        return value;
    }

    /**
     * 枚举转map
     */
    public static <T> Map<Integer, String> enumToMap(Class<T> enumClass) {
        Map<Integer, String> enumMap = new LinkedHashMap<>();
        if (!enumClass.isEnum()) {
            return enumMap;
        }
        T[] enums = enumClass.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enumMap;
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            enumMap.put((Integer) getMethodValue(keyMethod, tobj), (String) getMethodValue(valueMethod, tobj));
        }
        return enumMap;

    }

    /**
     * 根据反射，通过方法名称获取方法值，忽略大小写的
     *
     * @param methodName
     * @param obj
     * @param args
     * @return return value
     */
    private static <T> Object getMethodValue(String methodName, T obj, Object... args) {
        try {
            /** 获取方法数组，这里只要共有的方法 */
            Method[] methods = obj.getClass().getMethods();
            if (methods.length <= 0) {
                return null;
            }
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                /** 忽略大小写取方法 */
                if (methods[i].getName().equalsIgnoreCase(methodName)) {
                    /** 如果存在，则取出正确的方法名称 */
                    method = methods[i];
                    break;
                }
            }
            if (method == null) {
                return null;
            }
            return method.invoke(obj, args);
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
     * @param methodNames
     * @return enum description
     */
    private static <T> Object getEnumKeyValue(Object value, Class<T> enumClass, String... methodNames) {
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
                Object resultValue = getMethodValue(keyMethod, t);
                if (resultValue.equals(value)) {
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
