package org.lychee.web.validation.intecepter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.lychee.web.validation.constraints.Email;
import org.lychee.web.validation.constraints.Future;
import org.lychee.web.validation.constraints.Length;
import org.lychee.web.validation.constraints.Max;
import org.lychee.web.validation.constraints.Maxlength;
import org.lychee.web.validation.constraints.Min;
import org.lychee.web.validation.constraints.Minlength;
import org.lychee.web.validation.constraints.Mobile;
import org.lychee.web.validation.constraints.NotEmpty;
import org.lychee.web.validation.constraints.NotNull;
import org.lychee.web.validation.constraints.Past;
import org.lychee.web.validation.constraints.Phone;
import org.lychee.web.validation.constraints.Size;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lizhixiao
 * @date: 2018年1月25日
 * @Description:参数注解校验
 */
public class ValidateResoler implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object param) throws Exception {
		if (param instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) param;
			Email email = handlerMethod.getMethod().getAnnotation(Email.class);
			Future future = handlerMethod.getMethod().getAnnotation(Future.class);
			Length length = handlerMethod.getMethod().getAnnotation(Length.class);
			Max max = handlerMethod.getMethod().getAnnotation(Max.class);
			Maxlength maxlength = handlerMethod.getMethod().getAnnotation(Maxlength.class);
			Min min = handlerMethod.getMethod().getAnnotation(Min.class);
			Minlength minlength = handlerMethod.getMethod().getAnnotation(Minlength.class);
			Mobile mobile = handlerMethod.getMethod().getAnnotation(Mobile.class);
			NotEmpty notEmpty = handlerMethod.getMethod().getAnnotation(NotEmpty.class);
			NotNull notNull = handlerMethod.getMethod().getAnnotation(NotNull.class);
			Past past = handlerMethod.getMethod().getAnnotation(Past.class);
			org.lychee.web.validation.constraints.Pattern pattern = handlerMethod.getMethod()
					.getAnnotation(org.lychee.web.validation.constraints.Pattern.class);
			Phone phone = handlerMethod.getMethod().getAnnotation(Phone.class);
			Size size = handlerMethod.getMethod().getAnnotation(Size.class);
			if (email != null) {
				if (!isEmail(email.value(), email.required())) {
					return false;
				}
			}
			if (future != null) {
				if (!isFuture(future.value(), future.required())) {
					return false;
				}
			}
			if (length != null) {
				if (!length(length.value(), length.required())) {
					return false;
				}
			}
		} else {
			return true;
		}

		response.sendError(403, "Forbidden");
		return false;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	/** 判断是否是邮件 */
	private boolean isEmail(String email, boolean required) {
		if (!required && StringUtils.isEmpty(email))
			return true;
		if (StringUtils.isEmpty(email))
			return false;
		Pattern p = Pattern.compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");// 复杂匹配
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/** 判断是否未来日期 */
	private boolean isFuture(String date, boolean required) {
		if (!required && null == date)
			return true;
		if (null == date)
			return false;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			Date _date = sdf.parse(date);
			if (_date.getTime() > System.currentTimeMillis()) {
				return true;
			}
		} catch (ParseException e) {
		}
		return false;
	}

	/** 验证字符串输入长度或介于 m 和 n 之间的字符串(汉字算一个字符) */
	private boolean length(int[] lenth, boolean required) {
		if (!required && null == lenth)
			return true;
		if (null == lenth || lenth.length == 0)
			return false;
		return false;
	}
}
