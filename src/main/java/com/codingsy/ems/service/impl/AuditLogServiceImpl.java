package com.codingsy.ems.service.impl;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.codingsy.ems.model.AuditLog;
import com.codingsy.ems.repository.AuditLogRepository;
import com.codingsy.ems.service.AuditLogService;
import com.codingsy.ems.specification.AuditLogSpecification;

@Service
public class AuditLogServiceImpl implements AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogServiceImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
	public void logAction(String username, String action, String details) {
        AuditLog log = new AuditLog(username, action, details);
        auditLogRepository.save(log);
    }

	@Override
	public Page<AuditLog> getLogs(int page, int size, String sortBy, String direction) {
		
		validateDirection(direction);
		
		// Create the Sort object based on the direction
	    Sort sort = getSortDirection(sortBy, direction);
	    
	    Pageable pageable = PageRequest.of(page, size, sort);
	    
	    Page<AuditLog> all = auditLogRepository.findAll(pageable);
	    
	    //all.forEach(System.out::println);
	    
		return all;
	}

	private Sort getSortDirection(String sortBy, String direction) {
		Sort sort = direction.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
		return sort;
	}

	

	@Override
	public Page<AuditLog> getLogs(int page, int size, String sortBy, String direction, String username, String action,
			LocalDate startDate, LocalDate endDate, LocalDate date) {
		
		// Prioritize date over startDate/endDate
	    if (date != null) {
	        startDate = null;
	        endDate = null;
	    }
	    
		validateDirection(direction);
		
		validateStartAndEndDates(startDate, endDate);

	    // Validate future dates
	    validateFutureDates(startDate, endDate, date);
		
		// Create the Sort object based on the direction
	    Sort sort = getSortDirection(sortBy, direction);
	    
	    Pageable pageable = PageRequest.of(page, size, sort);
	    
	    Specification<AuditLog> spec = Specification
				.where(AuditLogSpecification.hasUsermae(username))
				.and(AuditLogSpecification.hasAction(action))
				.and(AuditLogSpecification.isBetweenTimeStamps(startDate, endDate)
				.and(AuditLogSpecification.isOnSpecificDate(date)));
	    
	    Page<AuditLog> all = auditLogRepository.findAll(spec, pageable);
	    
	    all.forEach(System.out::println);
	    
		return all;
	}

	private void validateFutureDates(LocalDate startDate, LocalDate endDate, LocalDate date) {
	    validateFutureDate(startDate, "startDate");
	    validateFutureDate(endDate, "endDate");
	    validateFutureDate(date, "date");
	}

	private void validateFutureDate(LocalDate date, String dateName) {
		LocalDate today = LocalDate.now();
		if (date != null && date.isAfter(today)) {
	        throw new IllegalArgumentException(dateName + " cannot be in the future.");
	    }
	}

	private void validateStartAndEndDates(LocalDate startDate, LocalDate endDate) {
		// Validate date range
	    validateDateRange(startDate, endDate);

	    // Validate missing date parameters
	    validateMissingDates(startDate, endDate);
	}

	private void validateMissingDates(LocalDate startDate, LocalDate endDate) {
		if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
	        throw new IllegalArgumentException("Both startDate and endDate must be provided.");
	    }
	    
	    if ((startDate != null && endDate == null) || (startDate != null && endDate == null)) {
	        throw new IllegalArgumentException("Both startDate and endDate must be provided.");
	    }
	}

	private void validateDateRange(LocalDate startDate, LocalDate endDate) {
		if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
	        throw new IllegalArgumentException("startDate must be before or equal to endDate.");
	    }
	    
	    if (startDate != null && endDate != null && endDate.isBefore(endDate)) {
	        throw new IllegalArgumentException("endDate must be after or equal to startDate.");
	    }
	}

	private void validateDirection(String direction) {
		if (!direction.equalsIgnoreCase("asc") && !direction.equalsIgnoreCase("desc")) {
	        throw new IllegalArgumentException("Invalid direction. Use 'asc' or 'desc'.");
	    }
	}
   
}
