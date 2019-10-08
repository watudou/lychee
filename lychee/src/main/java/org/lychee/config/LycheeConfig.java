package org.lychee.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 荔枝配置
 *
 * @author lizhixiao
 */
@Data
@Component
@ConfigurationProperties(prefix = "lychee", ignoreUnknownFields = true)
public class LycheeConfig {

    /**
     * 启用权限验证
     */
    private Boolean usePermission = false;
    private Boolean useRedis = false;
    private Boolean useTrasaction = false;
    /**
     * 权限拦截url
     */
    private String permissionPath = "/";
    /**
     * 忽略权限拦截url
     */
    private String permissionExcludePath;
    /**
     * 权限key
     */
    private String permissionKey = "permission:";
    /**
     * 启用OSS
     */
    private Boolean oss = true;
    /**
     * 启用验证码级别安全验证
     */
    private Boolean captchaSafe = false;
    /**
     * 验证码级别步长
     */
    private Integer captchaStep = 3;
    /**
     * 验证码有效期（秒）
     */
    private Integer captchaAge = 300;
    /**
     * cookie
     */
    private String captchaCookieName = "captcha";
}
