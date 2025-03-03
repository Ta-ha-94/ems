package com.codingsy.ems.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingsy.ems.constants.ApplicationConstants;
import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.exception.ResourceNotFoundException;
import com.codingsy.ems.mapper.EmployeeMapper;
import com.codingsy.ems.model.Employee;
import com.codingsy.ems.repository.EmployeeRepository;
import com.codingsy.ems.service.EmployeeService;
import com.codingsy.ems.service.AuditLogService;
import com.codingsy.ems.specification.EmployeeSpecification;

import jakarta.validation.Valid;

/**
 * Service implementation for managing employee-related operations.
 * This class provides methods for saving, retrieving, updating, and deleting employees,
 * as well as filtering and restoring deactivated employees.
 * It integrates with {@link EmployeeRepository}, {@link EmailService}, and {@link AuditLogService}
 * to perform database operations, send emails, and log actions for auditing purposes.
 * 
 * @author Taha
 * @version 1.0
 */	
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private static final Logger log = LoggerFactory.getLogger(EmployeeServiceImpl.class);
	
	private final EmployeeRepository employeeRepository;
	private final EmailService mailService;
	private final AuditLogService auditLogService;
	
	 /**
     * Constructs a new {@code EmployeeServiceImpl} with the required dependencies.
     *
     * @param employeeRepository the repository for employee data access.
     * @param mailService the service for sending emails.
     * @param auditLogService the service for logging audit actions.
     */
	public EmployeeServiceImpl(EmployeeRepository employeeRepository, EmailService mailService,
			AuditLogService auditLogService) {
		this.employeeRepository = employeeRepository;
		this.mailService = mailService;
		this.auditLogService = auditLogService;
	}

	/**
     * Saves a new employee to the database.
     * Only users with the 'ADMIN' role can access this method.
     * This method also logs the action and sends a welcome email to the employee.
     *
     * @param employeeDTO the employee data to be saved.
     * @return the saved employee as a {@link EmployeeDTO}.
     */
	@Transactional
	@PreAuthorize(value = "hasRole('ADMIN')")
	@Override
	public EmployeeDTO saveEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        EmployeeDTO savedEmployee = EmployeeMapper.toDTO(employeeRepository.save(employee));
        
        log.info("Employee with ID {} saved", savedEmployee.getId());
        
        auditLoggerHelper(savedEmployee.getId(), ApplicationConstants.ACTION_EMPLOYEE_CREATED, savedEmployee.getName());
        
        log.info("Sending welcome email to employee with ID {}", savedEmployee.getId());
        mailService.sendEmail(savedEmployee.getEmail(), savedEmployee.getName());// Send email asynchronously
        
        return savedEmployee;
    }

	/**
     * Retrieves a paginated list of all employees.
     * Users with either 'ADMIN' or 'EMPLOYEE' roles can access this method.
     *
     * @param page the page number (zero-based).
     * @param size the number of items per page.
     * @param sortBy the field to sort the results by.
     * @return a {@link Page} of {@link EmployeeDTO} containing the paginated and sorted employee data.
     */
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'EMPLOYEE')")
    @Override
    public Page<EmployeeDTO> getAllEmployees(int page, int size, String sortBy) {
    	Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    	
//    	Spring Data JPA’s Page<T> is not a List, it’s a special wrapper that:
//		✅ Already has a .map(Function<T, R> mapper) method.
//		✅ Allows direct transformations without calling .stream().
//    	It internally calls .stream().map() on the page content!
//		PageImpl<>(getContent().stream().map(converter).toList(), getPageable(), getTotalElements());    	
        return employeeRepository.findAll(pageable).map(EmployeeMapper::toDTO);
    }

	/**
     * Retrieves an employee by their ID.
     * Users with either 'ADMIN' or 'EMPLOYEE' roles can access this method.
     *
     * @param id the ID of the employee to retrieve.
     * @return the employee as a {@link EmployeeDTO}.
     * @throws ResourceNotFoundException if no active employee is found with the given ID.
     */
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'EMPLOYEE')")
    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        return EmployeeMapper.toDTO(getEmployeeByIdUtil(id));
    }

	/**
     * Updates an existing employee's details.
     * Only users with the 'ADMIN' role can access this method.
     * This method also logs the action in the audit table.
     *
     * @param id the ID of the employee to update.
     * @param employeeDTO the updated employee data.
     * @return the updated employee as a {@link EmployeeDTO}.
     */
	@PreAuthorize(value = "hasRole('ADMIN')")
    @Transactional
    @Override
    public EmployeeDTO updateEmployee(Long id, @Valid EmployeeDTO employeeDTO) {
        Employee existingEmployee = getEmployeeByIdUtil(id);

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setSalary(employeeDTO.getSalary());

        log.info("Employee with ID {} updated", id);
        
        auditLoggerHelper(id, ApplicationConstants.ACTION_EMPLOYEE_UPDATED, existingEmployee.getName());
        
        return EmployeeMapper.toDTO(employeeRepository.save(existingEmployee));
    }

	/**
     * Deactivates an employee (soft delete).
     * Only users with the 'ADMIN' role can access this method.
     * This method also logs the action in the audit table.
     *
     * @param id the ID of the employee to deactivate.
     */
	@PreAuthorize(value = "hasRole('ADMIN')")
    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeByIdUtil(id);
        existingEmployee.setActive(false);
        employeeRepository.save(existingEmployee);
        log.info("Employee with ID {} deactivated", id);
        
        auditLoggerHelper(existingEmployee.getId(), ApplicationConstants.ACTION_EMPLOYEE_DELETED, existingEmployee.getName());
    }

	/**
     * Filters employees based on name, minimum salary, and maximum salary.
     * Users with either 'ADMIN' or 'EMPLOYEE' roles can access this method.
     *
     * @param name the name to filter by (optional).
     * @param minSalary the minimum salary to filter by (optional).
     * @param maxSalary the maximum salary to filter by (optional).
     * @param pageable the pagination and sorting information.
     * @return a {@link Page} of {@link EmployeeDTO} containing the filtered employee data.
     */
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'EMPLOYEE')")
	@Override
	public Page<EmployeeDTO> filterEmployee(String name, Double minSalary, Double maxSalary, Pageable pageable) {
		if (minSalary != null && maxSalary != null && minSalary > maxSalary) {
		    throw new IllegalArgumentException("minSalary cannot be greater than maxSalary");
		}

		Specification<Employee> spec = Specification
											.where(EmployeeSpecification.hasName(name))
											.and(EmployeeSpecification.hasMinSalary(minSalary))
											.and(EmployeeSpecification.hasMaxSalary(maxSalary));
		
		return employeeRepository.findAll(spec, pageable).map(EmployeeMapper::toDTO);
	}

	/**
     * Retrieves a paginated list of all active employees.
     * Users with either 'ADMIN' or 'EMPLOYEE' roles can access this method.
     *
     * @param page the page number (zero-based).
     * @param size the number of items per page.
     * @param sortBy the field to sort the results by.
     * @return a {@link Page} of {@link EmployeeDTO} containing the paginated and sorted active employee data.
     */
	@PreAuthorize(value = "hasAnyRole('ADMIN', 'EMPLOYEE')")
	@Override
	public Page<EmployeeDTO> getAllActiveEmployees(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return employeeRepository.findAllActive(pageable).map(EmployeeMapper::toDTO);
	}
	
	/**
     * Restores a deactivated employee by setting their active status to true.
     * Only users with the 'ADMIN' role can access this method.
     * This method also logs the action in the audit table.
     *
     * @param id the ID of the employee to restore.
     * @return the restored employee as a {@link EmployeeDTO}.
     * @throws ResourceNotFoundException if no employee is found with the given ID.
     * @throws IllegalStateException if the employee is already active.
     */
	@PreAuthorize(value = "hasRole('ADMIN')")
	@Transactional
	@Override
	public EmployeeDTO restoreEmployee(Long id) {
		Employee existingEmployee = employeeRepository
										.findById(id)
										.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));

		existingEmployee.setActive(true);
		Employee employee = employeeRepository.save(existingEmployee);
		
		log.info("Employee with ID {} restored", id);
		
		auditLogService.logAction(existingEmployee.getName(), "EMPLOYEE_RESTORED", "Restored employee ID: " + existingEmployee.getId());
		
		return EmployeeMapper.toDTO(employee);
	}
	
	/**
     * Helper method to retrieve an active employee by ID.
     *
     * @param id the ID of the employee to retrieve.
     * @return the active {@link Employee} entity.
     * @throws ResourceNotFoundException if no active employee is found with the given ID.
     */
	private Employee getEmployeeByIdUtil(Long id) {
		Employee employee = employeeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active employee not found with id: " + id));
		log.info("Employee with ID {} found", id);
		return employee;
	}
	
	/**
     * Helper method to log into the audit table.
     *
     * @param id the ID of the employee/user.
     * @param name of the employee/user.
     * @param action that has been performed on the employee/user.
     * @return void
     */
	private void auditLoggerHelper(Long id, String action, String name) {
		String[] actions = action.split("_");
		String message = actions[1] + " " + actions[0] + " with id: " + id;
		
		log.info("Logging the action in the audit table for {}", action);
        auditLogService.logAction(name, action, message.toUpperCase());
        log.info("Logged the action in the audit table for {}", action);
	}
}
