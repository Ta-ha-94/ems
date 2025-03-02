package com.codingsy.ems.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration class for the Codingsy Employee Management System (EMS) application,
 * providing Spring beans for HTTP communication and other infrastructure components.
 * <p>
 * This class is annotated with {@link Configuration} to indicate it defines bean
 * definitions for the Spring application context. It is typically used to configure
 * shared resources, such as REST clients, that can be injected into other components.
 * </p>
 *
 * @author Taha
 * @version 1.0
 * @see Configuration
 * @since 1.0
 */
@Configuration
public class AppConfig {
    
	/**
     * Creates and registers a {@link RestTemplate} bean for HTTP communication.
     * <p>
     * This bean provides a simple, template-based approach to perform HTTP requests
     * (e.g., GET, POST) to external services. It is suitable for RESTful service
     * interactions within the EMS application, such as API calls to external systems.
     * The default {@link RestTemplate} instance uses standard HTTP client settings,
     * but can be customized for timeouts, proxies, or other configurations if needed.
     * </p>
     * <p>
     * Example usage in a service:
     * <pre>
     * &#64;Autowired
     * private RestTemplate restTemplate;
     *
     * public String fetchData(String url) {
     *     return restTemplate.getForObject(url, String.class);
     * }
     * </pre>
     * </p>
     *
     * @return a new instance of {@link RestTemplate} configured for HTTP requests
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}