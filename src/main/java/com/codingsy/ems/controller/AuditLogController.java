package com.codingsy.ems.controller;

import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import com.codingsy.ems.model.AuditLog;
import com.codingsy.ems.service.AuditLogService;

/**
 * This class is a REST controller for managing audit logs.
 * It provides end-points for retrieving and filtering audit logs.
 * Access to these end-points is restricted to users with the 'ADMIN' role.
 * 
 * @author Taha
 * @version 1.0
 */
@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    /**
     * Constructs a new AuditLogController with the specified AuditLogService.
     *
     * @param auditLogService The service used to perform audit log-related operations.
     */
    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

//    /audit/logs?page=0&size=5
//    /audit/logs?sortBy=timestamp&direction=asc&page=0&size=10
//    /audit/logs?sortBy=action&direction=asc&page=0&size=10
    /**
     * Retrieves a paginated list of all audit logs.
     * The results can be sorted by a specified field and direction.
     *
     * @param page The page number (default is 0).
     * @param size The number of logs per page (default is 10).
     * @param sortBy The field to sort by (default is "timestamp").
     * @param direction The sorting direction (default is "desc").
     * @return A ResponseEntity containing a page of AuditLog objects.
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditLog>> getAllLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Page<AuditLog> logs = auditLogService.getLogs(page, size, sortBy, direction);
        return ResponseEntity.ok(logs);
    }
    
//    /audit/filter?username=admin&page=0&size=10
//    /audit-logs/filter?startDate=2025-02-21&endDate=2025-02-21&action=EmploYEe&username=tannu&page=0&size=10
    /**
     * Filters audit logs based on various criteria such as username, action, date range, and specific date.
     * The results are paginated and can be sorted by a specified field and direction.
     *
     * @param page The page number (default is 0).
     * @param size The number of logs per page (default is 10).
     * @param sortBy The field to sort by (default is "timestamp").
     * @param direction The sorting direction (default is "desc").
     * @param username The username to filter by (optional).
     * @param action The action to filter by (optional).
     * @param startDate The start date of the range to filter by (optional).
     * @param endDate The end date of the range to filter by (optional).
     * @param date The specific date to filter by (optional).
     * @return A ResponseEntity containing a page of filtered AuditLog objects.
     */
    @GetMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AuditLog>> filterAuditLog(
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "10") int size,
    		@RequestParam(defaultValue = "timestamp") String sortBy,
    		@RequestParam(defaultValue = "desc") String direction,
    		@RequestParam(required = false) String username,
    		@RequestParam(required = false) String action,
    		@RequestParam(required = false) LocalDate startDate,
    		@RequestParam(required = false) LocalDate endDate,
    		@RequestParam(required = false) LocalDate date
    		){
    	
    	Page<AuditLog> logs = auditLogService.getLogs(page, size, sortBy, direction, username, action, startDate, endDate, date);
    	return ResponseEntity.ok(logs);
    }

}
