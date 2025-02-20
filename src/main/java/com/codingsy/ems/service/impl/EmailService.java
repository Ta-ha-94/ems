package com.codingsy.ems.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
	private final RestTemplate restTemplate;
	private final ResourceLoader resourceLoader;
    private final String mailSenderApiUrl = "http://localhost:8899/api/v1/email/send"; // Replace with actual URL

    @Async
    public void sendEmail(String to, String name) {
    	String subject = "Welcome!";
        String htmlContent = loadTemplate("templates/welcome-email.html", Map.of("name", name));

        if (htmlContent == null) {
            log.error("Failed to load email template. Email not sent.");
            return;
        }

        log.info("to: {}", to);
        log.info("subject: {}", subject);
        log.info("htmlContent: {}", htmlContent);
		
        Map<String, String> request = new HashMap<>();
        request.put("to", to);
        request.put("subject", subject);
        request.put("message", htmlContent);

        try {
            ResponseEntity<Void> response = restTemplate.postForEntity(mailSenderApiUrl, request, Void.class);
            log.info("Email sent to {}. Response: {}", to, response.getStatusCode());
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", to, e.getMessage());
        }
    }
    
    private String loadTemplate(String path, Map<String, String> values) {
        try {
            Resource resource = resourceLoader.getResource("classpath:" + path);
            String content = new String(resource.getInputStream().readAllBytes(), StandardCharsets.UTF_8);

            // Replace placeholders with actual values
            for (Map.Entry<String, String> entry : values.entrySet()) {
                content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
            }

            return content;
        } catch (IOException e) {
            throw new RuntimeException("Error loading email template", e);
        }
    }
}
