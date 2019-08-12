package org.lychee.web.captcha;

import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

import org.lychee.cache.EHCacheService;
import org.lychee.constant.LycheeConstant;

/** 验证码工具类 */
public class Captcha extends AbstractCaptcha {

	private static final Integer quota = 3;

	/** 输出验证码 */
	public static void writeCaptcha(String loginName, HttpServletResponse response) throws IOException {
		int validateQTY = getValidateQTY(loginName);
		int lengh = validateQTY / quota + 4;
		response.setHeader("Pragma", "No-cache");
		response.setHeader("Cache-Control", "no-cache");
		response.setDateHeader("Expires", 0);
		response.setContentType("image/png");
		String codeStr = genCodeStr(lengh);
		EHCacheService.put(LycheeConstant.CHACHE_CAPTHA, loginName, codeStr);
		drawImg(codeStr);
		OutputStream os = response.getOutputStream();
		ImageIO.write(image, "JPEG", os);
		image.flush();
		if (os != null) {
			os.close();
		}
	}

	/** 验证码校验次数 */
	public static Integer getValidateQTY(String loginName) {
		if (null == loginName) {
			return 0;
		}
		Integer val = (Integer) EHCacheService.get(LycheeConstant.LOGIN_CHECK_QTY, loginName);
		if (null == val) {
			return 0;
		}
		return val;
	}

	/** 验证码校验 */
	public static boolean validate(String loginName, String captcha) {
		int validateQTY = getValidateQTY(loginName);
		if (validateQTY / quota == 0) {
			return true;
		}
		String val = (String) EHCacheService.get(LycheeConstant.CHACHE_CAPTHA, loginName);
		if (null != val && val.equals(captcha)) {
			Integer qty = (Integer) EHCacheService.get(LycheeConstant.LOGIN_CHECK_QTY, loginName);
			if (null == qty)
				qty = 0;
			EHCacheService.put(LycheeConstant.LOGIN_CHECK_QTY, loginName, qty + 1);
			return true;
		}
		return false;
	}
}
