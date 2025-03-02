package com.codingsy.ems.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.codingsy.ems.model.AuditLog;

public interface AuditLogService {
	void logAction(String username, String action, String details);
	Page<AuditLog> getLogs(int page, int size, String sortBy, String direction);
	Page<AuditLog> getLogs(int page, int size, String sortBy, String direction, String username, String action,
			LocalDate startDate, LocalDate endDate, LocalDate date);
}