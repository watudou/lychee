package org.lychee.web.util;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class StringEnumUtil {

    /**
     * 获取key
     */
    public static <T> String getKey(String value, Class<T> enumT) {
        String key = (String) StringEnumUtil.getEnumDescriotionByValue(value, enumT, "value", "key");
        return key;
    }

    public static <T> String getValue(String key, Class<T> enumT) {
        String value = (String) StringEnumUtil.getEnumDescriotionByValue(key, enumT);
        return value;
    }

    /**
     * 枚举转map
     */
    public static <T> Map<String, String> EnumToMap(Class<T> enumT) {
        Map<String, String> enummap = new HashMap<String, String>();
        if (!enumT.isEnum()) {
            return enummap;
        }
        T[] enums = enumT.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return enummap;
        }
        /** 默认接口value方法 */
        String valueMathod = "key";
        /** 默认接口getValue方法 */
        String desMathod = "value";
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            /** 获取key值 */
            String resultValue = (String) getMethodValue(valueMathod, tobj);
            if (null == resultValue) {
                continue;
            }
            /** 获取getDesc描述值 */
            String resultDes = (String) getMethodValue(desMathod, tobj);
            /** 如果描述不存在获取属性值 */
            if (null == resultDes) {
                resultDes = null;
            }
            enummap.put(resultValue, resultDes);
        }
        return enummap;

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
        Object resut = "";
        try {
            /** 获取方法数组，这里只要共有的方法 */
            Method[] methods = obj.getClass().getMethods();
            if (methods.length <= 0) {
                return resut;
            }
            Method method = null;
            for (int i = 0, len = methods.length; i < len; i++) {
                /** 忽略大小写取方法 */
                if (methods[i].getName().equalsIgnoreCase(methodName)) {
                    /** 如果存在，则取出正确的方法名称 */
                    methodName = methods[i].getName();
                    method = methods[i];
                    break;
                }
            }
            if (method == null) {
                return resut;
            }
            /** 方法执行 */
            resut = method.invoke(obj, args);
            /** 返回结果 */
            return resut;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resut;
    }

    /**
     * 通过value值获取对应的描述信息
     *
     * @param value
     * @param enumT
     * @param methodNames
     * @return enum description
     */
    private static <T> Object getEnumDescriotionByValue(Object value, Class<T> enumT, String... methodNames) {
        /** 不是枚举则返回"" */
        if (!enumT.isEnum()) {
            return null;
        }
        /** 获取枚举的所有枚举属性，似乎这几句也没啥用，一般既然用枚举，就一定会添加枚举属性 */
        T[] enums = enumT.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return null;
        }
        int count = methodNames.length;
        /** 默认获取枚举value方法，与接口方法一致 */
        String valueMathod = "key";
        /** 默认获取枚举getDesc方法 */
        String desMathod = "value";
        if (count >= 1 && !"".equals(methodNames[0])) {
            valueMathod = methodNames[0];
        }
        if (count == 2 && !"".equals(methodNames[1])) {
            desMathod = methodNames[1];
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T t = enums[i];
            try {
                /** 获取枚举对象value */
                Object resultValue = getMethodValue(valueMathod, t);
                if (resultValue.toString().equals(value + "")) {
                    /** 存在则返回对应描述 */
                    Object resultDes = getMethodValue(desMathod, t);
                    return resultDes;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 通过枚举value或者自定义值及方法获取枚举属性值
     *
     * @param value
     * @param enumT
     * @param methodNames
     * @return enum key
     */
    public static <T> String getEnumKeyByValue(Object value, Class<T> enumT, String... methodNames) {
        if (!enumT.isEnum()) {
            return "";
        }
        T[] enums = enumT.getEnumConstants();
        if (enums == null || enums.length <= 0) {
            return "";
        }
        int count = methodNames.length;
        /** 默认方法 */
        String valueMathod = "key";
        /** 独立方法 */
        if (count >= 1 && !"".equals(methodNames[0])) {
            valueMathod = methodNames[0];
        }
        for (int i = 0, len = enums.length; i < len; i++) {
            T tobj = enums[i];
            try {
                Object resultValue = getMethodValue(valueMathod, tobj);
                /** 存在则返回对应值 */
                if (resultValue != null && resultValue.toString().equals(value + "")) {
                    return tobj + "";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

}
