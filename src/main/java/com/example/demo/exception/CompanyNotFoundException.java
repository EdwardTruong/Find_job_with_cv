package com.example.demo.exception;

public class CompanyNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8068181597966486884L;

	public CompanyNotFoundException() {
		super();
	}

	public CompanyNotFoundException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public CompanyNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

	public CompanyNotFoundException(String message) {
		super(message);
	}

	public CompanyNotFoundException(Throwable cause) {
		super(cause);
	}
	
	
}
