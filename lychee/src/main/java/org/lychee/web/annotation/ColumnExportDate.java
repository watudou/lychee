package org.lychee.web.annotation;

import java.lang.annotation.*;

/**
 * excel导出字段注解
 * @author lizhixiao
 */
@Documented
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ColumnExportDate {

    /**
     *  从程序导出的类型
     * @return
     */
    String value() default "yyyy-MM-dd HH:mm:ss";
}