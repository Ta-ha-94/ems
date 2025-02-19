package com.codingsy.ems.security.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.codingsy.ems.constants.ApplicationConstants;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filter responsible for validating JWT tokens in the request header.
 */
public class JwtValidationFilter extends OncePerRequestFilter {
	
	private static final Logger logger = LoggerFactory.getLogger(JwtGeneratorFilter.class);
	
	/**
     * Validates the JWT token in the request header and sets the authentication context.
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
		try {
			logger.info("JWT validation starts");
			
			String jwt = request.getHeader(ApplicationConstants.JWT_HEADER);
			if (jwt == null) {
	            logger.debug("JWT header is missing in the request header.");
	            throw new BadCredentialsException("JWT header is missing in the request header.");
	        }
			
			logger.info("JWT token fetched from the header.");
			
			Environment env = getEnvironment();
            if (env == null) {
                logger.error("Environment is null during JWT validation.");
                throw new IllegalStateException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
            }
            
            logger.info("Environment loaded.");
            
            String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY, ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
			if (secret == null || secret.isEmpty() || secret.isBlank()) {
	            logger.error("JWT secret is null or empty or blank during JWT generation");
	            throw new IllegalStateException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
	        }
			
			logger.info("JWT secret loaded from environment.");
			
			SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
			if (secretKey == null){
	            logger.error("SecretKey is null during JWT generation");
	            throw new IllegalStateException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
	        }
			
			logger.info("JWT SecretKey generated.");
			
			Claims payload = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(jwt).getPayload();
			if(payload == null) {
				logger.error("Payload is null in the JWT token.");
	            throw new BadCredentialsException(ApplicationConstants.JWT_GENERIC_EXCEPTION_MESSAGE);
			}
			
			logger.info("JWT paylaod parsed.");
			
			String username = String.valueOf(payload.get("username"));
			if (username == null || username.isEmpty() || username.isBlank()) {
	            logger.error("Username missing in the payload");
	            throw new BadCredentialsException("Malformed token");
	        }
			
			logger.info("JWT claim username parsed is: {}", username);
			
			String authorities = String.valueOf(payload.get("authorities"));
			if(authorities == null) {
				logger.error("Authorities missing in the payload");
	            throw new BadCredentialsException("Malformed token");
			}
			
			logger.info("JWT claim authorities parsed for user: {}", username);
			
			Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, AuthorityUtils.commaSeparatedStringToAuthorityList(authorities));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			logger.debug("JWT validation successful for user: {}", username);
		}  catch (JwtException e) {
        	logger.error("Error generating JWT ", e);
        	throw new BadCredentialsException("Invalid JWT: " + e.getMessage());
        } catch (Exception e) {
        	logger.error("Error generating JWT", e);
        	throw new BadCredentialsException("Invalid JWT found in the request header!");
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
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return request.getServletPath().equals("/auth/login");
	}
}
