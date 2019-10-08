package org.lychee.service;

import org.apache.commons.lang.StringUtils;
import org.lychee.config.LycheeConfig;
import org.lychee.constant.LycheeConstant;
import org.lychee.web.captcha.AbstractCaptcha;
import org.lychee.web.request.IpHelper;
import org.lychee.web.util.CookieUtil;
import org.lychee.web.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

/**
 * 验证码工具类
 */
@Service
public class CaptchaService extends AbstractCaptcha {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private LycheeConfig lycheeConfig;
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 输出验证码
     */
    public void writeCaptcha() throws IOException {
        int lengh = getValidateQty() / lycheeConfig.getCaptchaStep() + 4;
        response.setHeader("Pragma", "No-cache");
        response.setHeader("Cache-Control", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/png");
        String codeStr = genCodeStr(lengh);
        saveCaptcha(codeStr);
        drawImg(codeStr);
        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "JPEG", os);
        image.flush();
        if (os != null) {
            os.close();
        }
    }

    /**
     * 验证码校验级别
     */
    public Integer getValidateQty() {
        if (!lycheeConfig.getCaptchaSafe()) {
            return 0;
        }
        String ip = IpHelper.getIpAddr(request);
        Integer ipQty = (Integer) redisTemplate.opsForValue().get(LycheeConstant.KEY_CAPTCHA_CHECK + ip);
        Integer qty = null;
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId)) {
            qty = (Integer) redisTemplate.opsForValue().get(LycheeConstant.KEY_CAPTCHA_CHECK + deviceId);
        } else {
            String key = CookieUtil.getCookieVal(request, lycheeConfig.getCaptchaCookieName());
            if (StringUtils.isNotBlank(key)) {
                qty = (Integer) redisTemplate.opsForValue().get(LycheeConstant.KEY_CAPTCHA_CHECK + key);
            }

        }
        if (null == qty) {
            qty = ipQty;
        }
        if (null == qty) {
            ipQty = 0;
            qty = 0;
        }
        if (ipQty > 60 && qty < ipQty && qty < 10) {
            qty = ipQty / 10 + qty;
        }
        return qty;
    }

    public Boolean incValidateQty() {
        if (!lycheeConfig.getCaptchaSafe()) {
            return true;
        }
        String keyIp = LycheeConstant.KEY_CAPTCHA_CHECK + IpHelper.getIpAddr(request);
        redisTemplate.expire(keyIp, lycheeConfig.getCaptchaAge(), TimeUnit.SECONDS);
        redisTemplate.opsForValue().increment(keyIp);
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId)) {
            redisTemplate.opsForValue().increment(LycheeConstant.KEY_CAPTCHA_CHECK + deviceId);
        } else {
            deviceId = CookieUtil.getCookieVal(request, lycheeConfig.getCaptchaCookieName());
            if (StringUtils.isNotBlank(deviceId)) {
                redisTemplate.opsForValue().increment(LycheeConstant.KEY_CAPTCHA_CHECK + deviceId);
            }
        }
        redisTemplate.expire(LycheeConstant.KEY_CAPTCHA_CHECK + deviceId, lycheeConfig.getCaptchaAge(), TimeUnit.SECONDS);
        return true;
    }

    /**
     * 验证码校验
     */
    public Boolean validate(String captcha) {
        if (lycheeConfig.getCaptchaSafe() && getValidateQty() / lycheeConfig.getCaptchaStep() == 0) {
            return true;
        }
        String capCode = null;
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId)) {
            capCode = (String) redisTemplate.opsForValue().get(LycheeConstant.KEY_CAPTHA + deviceId);
        } else {
            deviceId = CookieUtil.getCookieVal(request, lycheeConfig.getCaptchaCookieName());
            capCode = (String) redisTemplate.opsForValue().get(LycheeConstant.KEY_CAPTHA + deviceId);
        }
        if (null == capCode) {
            return null;
        }
        redisTemplate.delete(LycheeConstant.KEY_CAPTHA + deviceId);
        return capCode.equals(captcha);
    }

    private void saveCaptcha(String captcha) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId)) {
            redisTemplate.opsForValue().set(LycheeConstant.KEY_CAPTHA + deviceId, captcha, lycheeConfig.getCaptchaAge(), TimeUnit.SECONDS);
            return;
        }
        String key = CookieUtil.getCookieVal(request, lycheeConfig.getCaptchaCookieName());
        if (null == key) {
            key = TokenUtil.createToken();
            CookieUtil.createCookie(request, response, lycheeConfig.getCaptchaCookieName(), key, null, lycheeConfig.getCaptchaAge());
        }
        redisTemplate.opsForValue().set(LycheeConstant.KEY_CAPTHA + key, captcha, lycheeConfig.getCaptchaAge(), TimeUnit.SECONDS);
    }

}
