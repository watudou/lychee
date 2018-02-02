package org.lychee.web.validation.tmp;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.ValidatorFactory;
import javax.validation.executable.ExecutableValidator;

import org.springframework.core.MethodParameter;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lizhixiao
 * @date: 2018年1月25日
 * @Description:参数注解校验
 */
public class ValidateInterceptor implements HandlerInterceptor {

	private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
	private final ExecutableValidator validator = factory.getValidator().forExecutables();

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Annotation[] a = handlerMethod.getMethod().getAnnotations();
			Map<String, String[]> paramMap = request.getParameterMap();
			Object[] args = new String[handlerMethod.getMethodParameters().length];
			int i = 0;

			MethodParameter[] parameters = handlerMethod.getMethodParameters();
			for (MethodParameter methodParameter : handlerMethod.getMethodParameters()) {
				if (null != paramMap.get(methodParameter.getParameterName())) {
					args[i] = paramMap.get(methodParameter.getParameterName())[0];
				}
				i++;

			}
			Set<ConstraintViolation<Object>> validResult = validMethodParams(handlerMethod.getBean(),
					handlerMethod.getMethod(), args);
			if (validResult.size() > 0) {
				return false;
			}
		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3)
			throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
			throws Exception {

	}

	private <T> Set<ConstraintViolation<T>> validMethodParams(T obj, Method method, Object[] params) {
		return validator.validateParameters(obj, method, params);
	}

}
