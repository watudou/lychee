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

//================权限==============
    /**
     * 启用权限验证
     */
    private Boolean usePermission = false;
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
    //================验证码===================
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

    //==================短信=====================
    /**
     * IP用户每分钟短信最大发送数量
     */
    private Integer smsMaxSendQty = 60;
    /**
     * 用户短信最大发送数量时间限制（秒）
     */
    private Integer smsSendInterval = 60;
    /**
     * 用户短信有效期
     */
    private Integer smsCodeExpiration = 3600;
    /**
     * 短信key
     */
    private String smsCodeKey = "smsCode:";
    /**
     * 发送间隔
     */
    private String smsIntervalKey = "smsInterval:";
}
