package com.codingsy.ems.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.service.EmployeeService;

import jakarta.validation.Valid;

/**
 * This class is a REST controller for managing employees.
 * It provides endpoints for creating, retrieving, updating, deleting, and filtering employees.
 * 
 * @author Taha
 * @version 1.0
 */
@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;

	/**
     * Constructs a new EmployeeController with the specified EmployeeService.
     *
     * @param employeeService The service used to perform employee-related operations.
     */
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    /**
     * Creates a new employee.
     *
     * @param employeeDTO The employee data to be created.
     * @return A ResponseEntity containing the created employee and the location header.
     */
    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
    	EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        URI location = URI.create("/employees/" + savedEmployee.getId());
        return ResponseEntity.created(location).body(savedEmployee);
    }

    /**
     * Retrieves a paginated list of all active employees.
     *
     * @param page The page number (default is 0).
     * @param size The number of employees per page (default is 10).
     * @param sortBy The field to sort by (default is "id").
     * @return A ResponseEntity containing a page of EmployeeDTO objects.
     */
    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(employeeService.getAllActiveEmployees(page, size, sortBy));
    }

    /**
     * Retrieves an employee by their ID.
     *
     * @param id The ID of the employee to retrieve.
     * @return A ResponseEntity containing the EmployeeDTO object.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    /**
     * Updates an existing employee.
     *
     * @param id The ID of the employee to update.
     * @param employeeDTO The updated employee data.
     * @return A ResponseEntity containing the updated EmployeeDTO object.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    /**
     * Deletes an employee by their ID.
     *
     * @param id The ID of the employee to delete.
     * @return A ResponseEntity with a success message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully!");

        return ResponseEntity.ok().body(response);
    }
    
    /**
     * Filters employees based on name, salary range, and pagination.
     *
     * @param name The name to filter by (optional).
     * @param minSalary The minimum salary to filter by (optional).
     * @param maxSalary The maximum salary to filter by (optional).
     * @param page The page number (default is 0).
     * @param size The number of employees per page (default is 5).
     * @param sortBy The field to sort by (default is "id").
     * @return A ResponseEntity containing a page of filtered EmployeeDTO objects.
     */
    @GetMapping("/filter")
    public ResponseEntity<Page<EmployeeDTO>> filterEmployee(
    		@RequestParam(required = false) String name,
    		@RequestParam(required = false) Double minSalary,
    		@RequestParam(required = false) Double maxSalary,
    		@RequestParam(defaultValue = "0") int page,
    		@RequestParam(defaultValue = "5") int size,
    		@RequestParam(defaultValue = "id") String sortBy
    		){
    	Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
    	Page<EmployeeDTO> employees = employeeService.filterEmployee(name, minSalary, maxSalary, pageable);
    	return ResponseEntity.ok(employees);
    }
    
    @PutMapping("/{id}/restore")
    public ResponseEntity<EmployeeDTO> restoreEmployee(@PathVariable Long id){
    	return ResponseEntity.ok(employeeService.restoreEmployee(id));
    }
}
