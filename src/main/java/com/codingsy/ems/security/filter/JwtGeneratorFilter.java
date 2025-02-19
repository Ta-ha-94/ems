package com.codingsy.ems.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codingsy.ems.constants.ApplicationConstants;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter responsible for generating JWT tokens after successful authentication.
 */
public class JwtGeneratorFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtGeneratorFilter.class);

	/**
     * Generates a JWT token and sets it in the response header.
     *
     * @param request     The HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain.
     * @throws ServletException If a servlet-related exception occurs.
     * @throws IOException      If an I/O exception occurs.
     */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		Authentication authentication = null;
		try {
			logger.info("JWT generation starts");
			
			authentication = SecurityContextHolder.getContext().getAuthentication();
			if (authentication == null) {
	            logger.error("Authentication is null during JWT generation");
	            throw new IllegalStateException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
	        }
			
			logger.info("Authentication object fetched form SecurityContextHolder for user: {}", authentication.getName());
			
			Environment env = getEnvironment();
	        if (env == null) {
	            logger.error("Environment is null during JWT generation");
	            throw new IllegalStateException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
	        }
			
	        logger.info("Environment object loaded for user: {}", authentication.getName());
	        
			String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
			if (secret == null || secret.isEmpty() || secret.isBlank()) {
	            logger.error("JWT secret is null or empty or blank during JWT generation");
	            throw new IllegalArgumentException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
	        }
			
			logger.info("JWT secret loaded for user: {}", authentication.getName());
			
			SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
			if (secretKey == null){
	            logger.error("SecretKey is null during JWT generation");
	            throw new IllegalStateException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
	        }
			
			logger.info("JWT SecretKey generated for user: {}", authentication.getName());
			
			String jwt = Jwts
				.builder()
				.issuer(ApplicationConstants.JWT_ISSUER)
				.subject(ApplicationConstants.JWT_SUBJECT)
				.claim(ApplicationConstants.JWT_USERNAME_CLAIM, authentication.getName())
				.claim(ApplicationConstants.JWT_AUTHORITIES_CLAIM, authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
				.issuedAt(new Date())
				.expiration(new Date(new Date().getTime() + ApplicationConstants.JWT_EXPIRATION_MS)) //8 hours
				.signWith(secretKey)
				.compact();
			
			response.setHeader(ApplicationConstants.JWT_HEADER, jwt);
			logger.debug("JWT generated for user: {}", authentication.getName());
		} catch (JwtException e) {
        	logger.error("Error generating JWT for user: {}", authentication.getName(), e);
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT for user: " + authentication.getName());
            return;
        } catch (Exception e) {
        	logger.error("Error generating JWT for user: {}", authentication.getName(), e);
        	response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Some error occured for user: " + authentication.getName());
            return;
        }
		
		filterChain.doFilter(request, response);
	}
	
	/**
     * Determines whether the filter should be skipped for the given request.
     *
     * @param request The HTTP request.
     * @return {@code true} if the filter should be skipped, {@code false} otherwise.
     * @throws ServletException If a servlet-related exception occurs.
     */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return !request.getServletPath().equals("/auth/login");
	}

}
