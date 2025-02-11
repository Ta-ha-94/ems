package com.codingsy.ems.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.codingsy.ems.dto.EmployeeDTO;

public interface EmployeeService {
	EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    Page<EmployeeDTO> getAllEmployees(int page, int size, String sortBy);
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
    Page<EmployeeDTO> filterEmployee(String name, Double minSalary, Double maxSalary, Pageable pageable);
    Page<EmployeeDTO> getAllActiveEmployees(int page, int size, String sortBy);
    EmployeeDTO restoreEmployee(Long id);
}
