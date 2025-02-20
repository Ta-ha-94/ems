package com.codingsy.ems.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.codingsy.ems.model.AuditLog;
import java.util.List;


public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
	List<AuditLog> findByUsername(String username);
}
