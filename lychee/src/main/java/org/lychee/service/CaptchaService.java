package org.lychee.service;

import org.apache.commons.lang.StringUtils;
import org.lychee.config.LycheeConfig;
import org.lychee.web.captcha.AbstractCaptcha;
import org.lychee.web.request.IpHelper;
import org.lychee.web.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
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
    public void writeCaptcha(String token, HttpServletResponse response) throws IOException {
        int lengh = getValidateLevel(token) + 4;
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
    public Integer getValidateLevel(String token) {
        if (!lycheeConfig.getCaptchaSafe()) {
            return 0;
        }
        String key = getCookieVal(lycheeConfig.getCaptchaCookieName());
        if (null == key) {
            key = IpHelper.getIpAddr(request);
        }
        Integer qty = (Integer) redisTemplate.opsForValue().get(key);
        if (null == qty) {
            return 0;
        }
        return qty;
    }

    /**
     * 验证码校验
     */
    public Boolean validate(String captcha) {
        String capVal = null;
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId)) {
            capVal = (String) redisTemplate.opsForValue().get(deviceId);
        } else {
            capVal = getCookieVal(lycheeConfig.getCaptchaCookieName());
        }
        if (null == capVal) {
            return null;
        }
        return capVal.equals(captcha);
    }

    private void saveCaptcha(String captcha) {
        String deviceId = request.getParameter("deviceId");
        if (StringUtils.isNotBlank(deviceId)) {
            redisTemplate.opsForValue().set(deviceId, captcha, 3, TimeUnit.MINUTES);
            return;
        }
        String key = getCookieVal(lycheeConfig.getCaptchaCookieName());
        if (null == key) {
            key = createCookie(lycheeConfig.getCaptchaCookieName());
        }
        redisTemplate.opsForValue().set(key, captcha, 3, TimeUnit.MINUTES);
    }

    private String getCookieVal(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            Cookie c = cookies[i];
            if (c.getName().equals(name)) {
                return c.getValue();
            }
        }
        return null;
    }

    public String createCookie(String cookieName) {
        String ip = IpHelper.getIpAddr(request);
        String token = TokenUtil.createToken();
        Cookie cookie = new Cookie(cookieName, token);
        cookie.setMaxAge(lycheeConfig.getCaptchaAge());
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        return token;
    }
}
