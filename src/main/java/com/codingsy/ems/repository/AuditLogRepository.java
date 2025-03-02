package com.codingsy.ems.repository;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.codingsy.ems.model.AuditLog;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {

	// Find all logs with pagination and sorting
    Page<AuditLog> findAll(Pageable pageable);

    // Find logs by username (with pagination and sorting)
    Page<AuditLog> findByUsername(String username, Pageable pageable);

    // Find logs by action (with pagination and sorting)
    Page<AuditLog> findByAction(String action, Pageable pageable);

    // Find logs by username and action (with pagination and sorting)
    Page<AuditLog> findByUsernameAndAction(String username, String action, Pageable pageable);
    
    // Filter logs by username, action, and timestamp range
    Page<AuditLog> findByUsernameAndActionAndTimestampBetween(String username,
            String action,
            LocalDateTime startTimestamp,
            LocalDateTime endTimestamp,
            Pageable pageable);
}
