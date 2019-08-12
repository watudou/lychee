package org.lychee.framework;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** spring bean 持有类 */
public class SpringContextHolder implements ApplicationContextAware {

	private static ApplicationContext context;

	/**
	 * 获取 HttpServletRequest
	 */
	public static HttpServletRequest getHttpServletRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

	/**
	 * 获取 bean,需要注入org.lychee.framework.SpringContextHolder到spring容器
	 */
	public static <T> T getBean(Class<T> clazz) {
		checkApplicationContext();
		return context.getBean(clazz);
	}

	public void setApplicationContext(ApplicationContext ac) throws BeansException {
		context = ac;
	}

	public static ApplicationContext getApplicationContext() {
		checkApplicationContext();
		return context;
	}

	private static void checkApplicationContext() {
		if (context == null) {
			throw new LycheeException("applicaitonContext未注入,请在applicationContext.xml中定义SpringContextHolder");
		}
	}

}