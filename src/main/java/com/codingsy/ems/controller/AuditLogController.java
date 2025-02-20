package com.codingsy.ems.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;
import com.codingsy.ems.model.AuditLog;
import com.codingsy.ems.service.impl.AuditLogService;
import java.util.List;

@RestController
@RequestMapping("/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public List<AuditLog> getAllLogs() {
        return auditLogService.getLogs();
    }
}
