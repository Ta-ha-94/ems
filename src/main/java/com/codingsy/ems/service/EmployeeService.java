package com.codingsy.ems.service;

import java.util.List;

import com.codingsy.ems.dto.EmployeeDTO;

public interface EmployeeService {
	EmployeeDTO saveEmployee(EmployeeDTO employeeDTO);
    List<EmployeeDTO> getAllEmployees();
    EmployeeDTO getEmployeeById(Long id);
    EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO);
    void deleteEmployee(Long id);
}
