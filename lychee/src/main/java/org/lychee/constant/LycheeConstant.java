package org.lychee.constant;

/**
 * @author lizhixao
 */
public class LycheeConstant {

    /**
     * 检查验证码次数
     * captchaCheckQty:{deviceId}/{ip}
     */
    public static final String KEY_CAPTCHA_CHECK = "captchaCheckQty:";
    /**
     * 检查验证码缓存
     * captha:{deviceId}/{token}/{ip}
     */
    public static final String KEY_CAPTHA = "captha:";
    /**
     * 成功返回
     */
    public static final Integer RESPONSE_SUCCESS_CODE = 0;
    /**
     * 失败返回
     */
    public static final Integer RESPONSE_ERROR_CODE = 1;
}
