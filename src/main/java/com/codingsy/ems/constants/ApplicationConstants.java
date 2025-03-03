package com.codingsy.ems.constants;

public class ApplicationConstants {
	/**
	 * 	JWT related constants - start
	 */
	public static final String JWT_SECRET_KEY = "JWT_SECRET";
    public static final String JWT_SECRET_DEFAULT_VALUE = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4SDFGHJKpoidfhn3457d8g0awetnf";
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_ISSUER = "Codingsy";
    public static final String JWT_SUBJECT = "Json-Web-Token";
    public static final String JWT_USERNAME_CLAIM = "username";
    public static final String JWT_AUTHORITIES_CLAIM = "authorities";
    public static final long JWT_EXPIRATION_MS = 60 * 60 * 1000 * 8; // 8 hours in milliseconds
    public static final String JWT_GENERIC_EXCEPTION_MESSAGE = "Exception occurred while user authentication.";
    /**
	 * 	JWT related constants - end
	 */
    
    public static final String ROLE_PREFIX = "ROLE_";
    
    /**
     *	Actions related constants -start
     */
    public static final String ACTION_EMPLOYEE_CREATED = "EMPLOYEE_CREATED";
    public static final String ACTION_EMPLOYEE_UPDATED = "EMPLOYEE_UPDATED";
    public static final String ACTION_EMPLOYEE_DELETED = "EMPLOYEE_DELETED";
    public static final String ACTION_EMPLOYEE_RESTORED = "EMPLOYEE_RESTORED";
    public static final String ACTION_USER_REGISTERED = "USER_REGISTERED";
    /**
     *	Actions related constants -end
     */
}
