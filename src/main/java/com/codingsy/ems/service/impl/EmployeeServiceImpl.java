package com.codingsy.ems.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.exception.ResourceNotFoundException;
import com.codingsy.ems.mapper.EmployeeMapper;
import com.codingsy.ems.model.Employee;
import com.codingsy.ems.repository.EmployeeRepository;
import com.codingsy.ems.service.EmployeeService;
import com.codingsy.ems.specification.EmployeeSpecification;

import jakarta.validation.Valid;
	
@Service
public class EmployeeServiceImpl implements EmployeeService{
	
	private final EmployeeRepository employeeRepository;
	
	public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
		this.employeeRepository = employeeRepository;
	}

	@Override
	public EmployeeDTO saveEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        return EmployeeMapper.toDTO(employeeRepository.save(employee));
    }

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

    @Override
    public EmployeeDTO getEmployeeById(Long id) {
        Employee employee = getEmployeeByIdUtil(id);
        return EmployeeMapper.toDTO(employee);
    }

    @Override
    public EmployeeDTO updateEmployee(Long id, @Valid EmployeeDTO employeeDTO) {
        Employee existingEmployee = getEmployeeByIdUtil(id);

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setSalary(employeeDTO.getSalary());

        return EmployeeMapper.toDTO(employeeRepository.save(existingEmployee));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeByIdUtil(id);
        existingEmployee.setActive(false);
        employeeRepository.save(existingEmployee);
    }

	@Override
	public Page<EmployeeDTO> filterEmployee(String name, Double minSalary, Double maxSalary, Pageable pageable) {
		Specification<Employee> spec = Specification
											.where(EmployeeSpecification.hasName(name))
											.and(EmployeeSpecification.hasMinSalary(minSalary))
											.and(EmployeeSpecification.hasMaxSalary(maxSalary));
		
		return employeeRepository.findAll(spec, pageable).map(EmployeeMapper::toDTO);
	}

	@Override
	public Page<EmployeeDTO> getAllActiveEmployees(int page, int size, String sortBy) {
		Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
		return employeeRepository.findAllActive(pageable).map(EmployeeMapper::toDTO);
	}
	
	private Employee getEmployeeByIdUtil(Long id) {
		Employee employee = employeeRepository.findByIdAndActiveTrue(id)
                .orElseThrow(() -> new ResourceNotFoundException("Active employee not found with id: " + id));
		return employee;
	}
}
