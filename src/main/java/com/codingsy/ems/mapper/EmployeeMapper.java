package com.codingsy.ems.mapper;

import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.model.Employee;

public class EmployeeMapper {
	public static EmployeeDTO toDTO(Employee employee) {
        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getSalary()
        );
    }

    public static Employee toEntity(EmployeeDTO dto) {
        return new Employee(
                dto.getId(),
                dto.getName(),
                dto.getEmail(),
                dto.getSalary(),
                true
        );
    }
}
