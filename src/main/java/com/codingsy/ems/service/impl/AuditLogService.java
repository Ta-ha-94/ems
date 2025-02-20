package com.codingsy.ems.service.impl;

import org.springframework.stereotype.Service;
import com.codingsy.ems.model.AuditLog;
import com.codingsy.ems.repository.AuditLogRepository;
import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public void logAction(String username, String action, String details) {
        AuditLog log = new AuditLog(username, action, details);
        auditLogRepository.save(log);
    }

    public List<AuditLog> getLogs() {
        return auditLogRepository.findAll();
    }
}
