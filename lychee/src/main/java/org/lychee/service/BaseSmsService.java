package org.lychee.service;

import org.apache.commons.lang.StringUtils;
import org.lychee.config.LycheeConfig;
import org.lychee.web.request.IpHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.TimeUnit;

/**
 * @author lizhixiao
 * @Date: 2019/10/9
 * @Description: 短信发送父类
 */
public abstract class BaseSmsService {

    @Autowired
    private LycheeConfig lycheeConfig;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 发送短信
     *
     * @param templateCode
     * @param type
     * @param mobile
     * @param param
     * @return
     */
    public abstract Boolean send(String templateCode, Integer type, String mobile, String param);

    /**
     * 存储验证码
     *
     * @param type
     * @param code
     * @return
     */
    protected Boolean putCode(Integer type, String code) {
        String deviceId = request.getHeader("deviceId");
        String redisCodeIpKey = lycheeConfig.getSmsCodeKey() + type + ":" + IpHelper.getIpAddr(request);
        String redisCodeKey = lycheeConfig.getSmsCodeKey() + type + ":" + deviceId;
        String redisIntervalKey = lycheeConfig.getSmsIntervalKey() + type + ":" + deviceId;
        redisTemplate.opsForValue().increment(redisCodeIpKey, 1);
        redisTemplate.opsForValue().set(redisCodeIpKey, code, lycheeConfig.getSmsSendInterval(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(redisIntervalKey, code, lycheeConfig.getSmsSendInterval(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().set(redisCodeKey, code, lycheeConfig.getSmsCodeExpiration(), TimeUnit.SECONDS);
        return true;
    }

    /**
     * 检查验证码是否正确
     *
     * @param code
     * @return
     */
    public Boolean checkSmsCode(Integer type, String code) {
        String deviceId = request.getHeader("deviceId");
        String redisCodeKey = lycheeConfig.getSmsCodeKey() + type + ":" + deviceId;
        String smsCode = (String) redisTemplate.opsForValue().get(redisCodeKey);
        if (null == smsCode) {
            return null;
        }
        if (!smsCode.equals(code)) {
            return false;
        }
        redisTemplate.delete(redisCodeKey);
        return true;
    }

    /**
     * 检查是否能发送短信
     *
     * @param type 短信类型
     * @return
     */
    public Boolean sendAuth(Integer type) {
        String deviceId = request.getHeader("deviceId");
        String redisCodeIPKey = lycheeConfig.getSmsCodeKey() + type + ":" + IpHelper.getIpAddr(request);
        String redisIntervalKey = lycheeConfig.getSmsIntervalKey() + type + ":" + deviceId;
        if (StringUtils.isBlank(deviceId)) {
            return null;
        }
        String interval = (String) redisTemplate.opsForValue().get(redisIntervalKey);
        Integer qtyIp = (Integer) redisTemplate.opsForValue().get(redisCodeIPKey);
        if (null != interval) {
            return false;
        }
        if (null != qtyIp && qtyIp > lycheeConfig.getSmsMaxSendQty()) {
            return null;
        }
        return true;
    }

}
