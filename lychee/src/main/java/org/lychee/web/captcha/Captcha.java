package org.lychee.web.captcha;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lychee.cache.EHCacheService;
import org.lychee.constant.LycheeConstant;
import org.lychee.framework.SingletonFactory;

public class Captcha extends AbstractCaptcha {

	public static void writeCaptcha(HttpServletRequest request, HttpServletResponse response) throws IOException {
		Cookie[] cookies = request.getCookies();
		boolean hasToken = false;
		for (Cookie cookie : cookies) {
			if (LycheeConstant.LOGIN_TOKEN.equals(cookie.getName())) {
				hasToken = true;
			}
		}
		if (!hasToken) {
			Cookie cookie = new Cookie(LycheeConstant.LOGIN_TOKEN, "1234567890");
			cookie.setMaxAge(3 * 60);
			cookie.setHttpOnly(true);
			response.addCookie(cookie);
		}
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		String codeStr = genCodeStr(9);
		drawImg(codeStr);
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "JPEG", os);
		image.flush();
		if (os != null) {
			os.close();
		}
	}

	/** 校验验证码 */
	public static Boolean validateCaptcha(HttpServletRequest request, String loginName, String captcha) {
		EHCacheService eHCacheServiceImpl = SingletonFactory.getInstance(EHCacheService.class);
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			if (LycheeConstant.LOGIN_TOKEN.equals(cookie.getName())) {
				Object objValue = eHCacheServiceImpl.get(LycheeConstant.CHACHE_CAPTHA, LycheeConstant.LOGIN_TOKEN);
				if (null == objValue) {
					return false;
				}
				boolean result = objValue.equals(captcha);
				if (result) {
					eHCacheServiceImpl.evict(LycheeConstant.CHACHE_CAPTHA, loginName);
				}
				eHCacheServiceImpl.put(LycheeConstant.CHACHE_CAPTHA, loginName, getValidateQTY(loginName) + 1);
				return result;
			}
		}
		return false;
	}

	/** 校验次数 */
	public static Integer getValidateQTY(String loginName) {
		EHCacheService eHCacheServiceImpl = SingletonFactory.getInstance(EHCacheService.class);
		Integer val = (Integer) eHCacheServiceImpl.get(LycheeConstant.CHACHE_CAPTHA, loginName);
		if (null == val) {
			return 0;
		}
		return val;
	}

}
