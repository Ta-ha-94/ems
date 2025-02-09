package com.codingsy.ems.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.exception.ResourceNotFoundException;
import com.codingsy.ems.mapper.EmployeeMapper;
import com.codingsy.ems.model.Employee;
import com.codingsy.ems.repository.EmployeeRepository;
import com.codingsy.ems.service.EmployeeService;

import jakarta.validation.Valid;
	
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private final EmployeeRepository employeeRepository;
	
	@Autowired
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public EmployeeDTO saveEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        return EmployeeMapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(EmployeeMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        return EmployeeMapper.toDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, @Valid EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setSalary(employeeDTO.getSalary());

        return EmployeeMapper.toDTO(employeeRepository.save(existingEmployee));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Employee not found with ID: " + id));
        employeeRepository.delete(existingEmployee);
    }
}
