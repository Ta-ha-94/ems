package com.codingsy.ems.exception;

public class ResourceNotFoundException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 3370410074300136935L;

	public ResourceNotFoundException(String message) {
        super(message);
    }
}
