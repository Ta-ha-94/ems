package com.codingsy.ems.service;

import org.springframework.data.domain.Page;

import com.codingsy.ems.dto.EmployeeDTO;

public interface EmployeeService {
	EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    Page<EmployeeDTO> getAllEmployees(int page, int size, String sortBy);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
}
