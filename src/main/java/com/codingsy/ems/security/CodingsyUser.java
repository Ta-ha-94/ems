package com.codingsy.ems.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

public class CodingsyUser implements UserDetails {
	
	private static final long serialVersionUID = 1L; // Added serialVersionUID
	
	private static final Log logger = LogFactory.getLog(User.class);

	private String password;

	private final String username;

	private final List<GrantedAuthority> authorities;

	private final boolean accountNonExpired;

	private final boolean accountNonLocked;

	private final boolean credentialsNonExpired;

	private final boolean enabled;
	
	/**
     * Constructs the <code>CodingsyUsers</code> with the details required by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     *
     * @param username the username presented to the <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the <code>DaoAuthenticationProvider</code>
     * @param authorities the authorities that should be granted to the caller if they
     * presented the correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     * a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
    public CodingsyUser(String username, String password, List<GrantedAuthority> authorities) {
    	this(username, password, true, true, true, true, authorities);
    }
    
    /**
     * Constructs the <code>CodingsyUsers</code> with the details required by
     * {@link org.springframework.security.authentication.dao.DaoAuthenticationProvider}.
     *
     * @param username the username presented to the <code>DaoAuthenticationProvider</code>
     * @param password the password that should be presented to the <code>DaoAuthenticationProvider</code>
     * @param enabled set to <code>true</code> if the user is enabled
     * @param accountNonExpired set to <code>true</code> if the account has not expired
     * @param credentialsNonExpired set to <code>true</code> if the credentials have not expired
     * @param accountNonLocked set to <code>true</code> if the account is not locked
     * @param authorities the authorities that should be granted to the caller if they
     * presented the correct username and password and the user is enabled. Not null.
     * @throws IllegalArgumentException if a <code>null</code> value was passed either as
     * a parameter or as an element in the <code>GrantedAuthority</code> collection
     */
	public CodingsyUser(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			List<GrantedAuthority> authorities) {
		
		Assert.hasText(username, "Username cannot be null or empty");
        Assert.hasText(password, "Password cannot be null or empty");
        Assert.notNull(authorities, "Authorities cannot be null");
        
		this.username = username;
		this.password = password;
		this.enabled = enabled;
		this.accountNonExpired = accountNonExpired;
		this.credentialsNonExpired = credentialsNonExpired;
		this.accountNonLocked = accountNonLocked;
		this.authorities = Collections.unmodifiableList(authorities);
		
		logger.debug("Created UserDetails for user: " + username);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	/**
	 * Indicates whether the user's account has expired. An expired account cannot be
	 * authenticated.
	 * @return <code>true</code> if the user's account is valid (ie non-expired),
	 * <code>false</code> if no longer valid (ie expired)
	 */
	@Override
	public String getUsername() {
		return this.username;
	}
	
	@Override
	public boolean isAccountNonExpired() {
		return this.accountNonExpired;
	}

	/**
	 * Indicates whether the user is locked or unlocked. A locked user cannot be
	 * authenticated.
	 * @return <code>true</code> if the user is not locked, <code>false</code> otherwise
	 */
	@Override
	public boolean isAccountNonLocked() {
		return this.accountNonLocked;
	}

	/**
	 * Indicates whether the user's credentials (password) has expired. Expired
	 * credentials prevent authentication.
	 * @return <code>true</code> if the user's credentials are valid (ie non-expired),
	 * <code>false</code> if no longer valid (ie expired)
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return this.credentialsNonExpired;
	}

	/**
	 * Indicates whether the user is enabled or disabled. A disabled user cannot be
	 * authenticated.
	 * @return <code>true</code> if the user is enabled, <code>false</code> otherwise
	 */
	@Override
	public boolean isEnabled() {
		return this.enabled;
	}
}
