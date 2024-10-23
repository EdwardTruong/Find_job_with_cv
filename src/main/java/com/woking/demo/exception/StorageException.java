package com.woking.demo.exception;

public class StorageException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 7517633346857325147L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
