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

@RestController
@RequestMapping("/employees")
public class EmployeeController {
	
	private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> createEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
    	EmployeeDTO savedEmployee = employeeService.saveEmployee(employeeDTO);
        URI location = URI.create("/employees/" + savedEmployee.getId());
        return ResponseEntity.created(location).body(savedEmployee);
    }

    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAllEmployees(
    		@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy) {
        return ResponseEntity.ok(employeeService.getAllActiveEmployees(page, size, sortBy));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.getEmployeeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(@PathVariable Long id, @Valid @RequestBody EmployeeDTO employeeDTO) {
        return ResponseEntity.ok(employeeService.updateEmployee(id, employeeDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteEmployee(@PathVariable Long id) {
        employeeService.deleteEmployee(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Employee deleted successfully!");

        return ResponseEntity.ok().body(response);
    }
    
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
