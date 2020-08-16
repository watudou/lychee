package org.lychee.bean;

import lombok.Data;

/**
 * 文件信息
 *
 * @author yons
 */
@Data
public class FileInfo {

    /**
     * 文件名
     */
    private String name;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 文件名后缀
     */
    private String suffix;
    /**
     * 文件地址
     */
    private String url;
}
