package org.lychee.web.validation.intecepter;

import org.lychee.web.validation.constraints.Size;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.ValueConstants;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.mvc.method.annotation.PathVariableMethodArgumentResolver;

/**
 * @author lizhixiao
 * @date: 2018年1月26日
 * @Description:TODO
 */
public class IdentityHandlerMethodArgumentResolver extends PathVariableMethodArgumentResolver {
	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		if (!parameter.hasParameterAnnotation(Size.class)) {
			return false;
		}
		return true;
	}

	@SuppressWarnings("synthetic-access")
	@Override
	protected NamedValueInfo createNamedValueInfo(MethodParameter parameter) {
		Size annotation = parameter.getParameterAnnotation(Size.class);
		return new PathVariableNamedValueInfo(annotation);
	}

	@Override
	protected Object resolveName(String name, MethodParameter parameter, NativeWebRequest request) throws Exception {
		Object obj = super.resolveName(name, parameter, request);
		if (obj != null) {
			try {
				// return Base58.decode(obj.toString());
			} catch (Exception e) {
				// ignore
			}
		}
		return obj;
	}

	public static class PathVariableNamedValueInfo extends NamedValueInfo {
		private PathVariableNamedValueInfo(Size annotation) {
			super(annotation.value(), true, ValueConstants.DEFAULT_NONE);
		}
	}

}
