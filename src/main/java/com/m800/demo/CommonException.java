package com.m800.demo;
/***
 * Custom Exception class for Application
 * @author vkanuri
 *
 */
public class CommonException extends Exception {

	private static final long serialVersionUID = -6434866126850991336L;

	public CommonException() {
	}

	public CommonException(String message) {
		super(message);
	}

	public CommonException(Throwable cause) {
		super(cause);
	}

	public CommonException(String message, Throwable cause) {
		super(message, cause);
	}

	public CommonException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
