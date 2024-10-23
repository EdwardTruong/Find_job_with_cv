package com.woking.demo.exception;

public class  StorageFileNotFoundException extends StorageException {
    
	/**
	 * 
	 */
	private static final long serialVersionUID = -1006360173210213447L;

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}

}
