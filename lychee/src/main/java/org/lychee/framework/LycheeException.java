package org.lychee.framework;

/** 异常处理类 */
public class LycheeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public LycheeException(String message) {
		super(message);
	}

	public LycheeException(Throwable throwable) {
		super(throwable);
	}

	public LycheeException(String message, Throwable throwable) {
		super(message, throwable);
	}
}
