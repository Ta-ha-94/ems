package com.codingsy.ems.exception;

public class UsernameUnavailableException extends RuntimeException {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6072123479896101108L;

	public UsernameUnavailableException(String message) {
        super(message);
    }
}
