package com.codingsy.ems.security.handler;

import java.util.function.Supplier;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.CsrfTokenRequestHandler;
import org.springframework.security.web.csrf.XorCsrfTokenRequestAttributeHandler;
import org.springframework.util.StringUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Custom CSRF Token Request Handler for Single-Page Applications (SPA).
 * <p>
 * This handler combines two strategies for handling CSRF tokens:
 * <ul>
 *   <li>{@link XorCsrfTokenRequestAttributeHandler} - Provides BREACH protection when rendering tokens in response.</li>
 *   <li>{@link CsrfTokenRequestAttributeHandler} - Used when handling CSRF tokens from request headers.</li>
 * </ul>
 * <p>
 * The implementation ensures:
 * <ul>
 *   <li>CSRF tokens are always written securely in responses.</li>
 *   <li>CSRF tokens are extracted correctly from request headers or parameters.</li>
 * </ul>
 * </p>
 *
 * @author Taha
 * @version 1.0
 * @since 1.0
 */
public class SpaCsrfTokenRequestHandler implements CsrfTokenRequestHandler{
	
	private final CsrfTokenRequestHandler plain = new CsrfTokenRequestAttributeHandler();
	private final CsrfTokenRequestHandler xor = new XorCsrfTokenRequestAttributeHandler();

	/**
     * Handles CSRF token processing for incoming requests.
     * <p>
     * - Uses {@code XorCsrfTokenRequestAttributeHandler} to mitigate BREACH attacks.
     * - Ensures the CSRF token is included in the response as a cookie.
     * </p>
     *
     * @param request   The incoming HTTP request.
     * @param response  The HTTP response where CSRF tokens are written.
     * @param csrfToken A supplier providing the CSRF token.
     */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, Supplier<CsrfToken> csrfToken) {
		/*
		 * Always use XorCsrfTokenRequestAttributeHandler to provide BREACH protection of
		 * the CsrfToken when it is rendered in the response body.
		 */
		this.xor.handle(request, response, csrfToken);
		/*
		 * Render the token value to a cookie by causing the deferred token to be loaded.
		 */
		csrfToken.get();
	}
	
	/**
     * Resolves the CSRF token from the request.
     * <p>
     * - Uses {@code CsrfTokenRequestAttributeHandler} if the request contains a CSRF header.
     * - Uses {@code XorCsrfTokenRequestAttributeHandler} if the CSRF token is passed as a request parameter.
     * </p>
     *
     * @param request   The HTTP request containing the CSRF token.
     * @param csrfToken The expected CSRF token.
     * @return The resolved CSRF token value.
     */
	@Override
	public String resolveCsrfTokenValue(HttpServletRequest request, CsrfToken csrfToken) {
		String headerValue = request.getHeader(csrfToken.getHeaderName());
		/*
		 * If the request contains a request header, use CsrfTokenRequestAttributeHandler
		 * to resolve the CsrfToken. This applies when a single-page application includes
		 * the header value automatically, which was obtained via a cookie containing the
		 * raw CsrfToken.
		 *
		 * In all other cases (e.g. if the request contains a request parameter), use
		 * XorCsrfTokenRequestAttributeHandler to resolve the CsrfToken. This applies
		 * when a server-side rendered form includes the _csrf request parameter as a
		 * hidden input.
		 */
		return (StringUtils.hasText(headerValue) ? this.plain : this.xor).resolveCsrfTokenValue(request, csrfToken);
	}

}
