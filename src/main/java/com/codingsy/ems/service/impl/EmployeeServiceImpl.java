package com.codingsy.ems.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.exception.ResourceNotFoundException;
import com.codingsy.ems.mapper.EmployeeMapper;
import com.codingsy.ems.model.Employee;
import com.codingsy.ems.repository.EmployeeRepository;
import com.codingsy.ems.service.EmployeeService;
import com.codingsy.ems.specification.EmployeeSpecification;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
	
@Service
@Slf4j
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService{
	
	private final EmployeeRepository employeeRepository;
	private final EmailService mailService;

	@Transactional
	@Override
	public EmployeeDTO saveEmployee(@Valid EmployeeDTO employeeDTO) {
        Employee employee = EmployeeMapper.toEntity(employeeDTO);
        EmployeeDTO savedEmployee = EmployeeMapper.toDTO(employeeRepository.save(employee));
        
        log.info("Employee with ID {} saved", savedEmployee.getId());
        log.info("Sending welcome email to employee with ID {}", savedEmployee.getId());
        
        mailService.sendEmail(savedEmployee.getEmail(), savedEmployee.getName());// Send email asynchronously
        
        return savedEmployee;
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
        return EmployeeMapper.toDTO(getEmployeeByIdUtil(id));
    }

    @Transactional
    @Override
    public EmployeeDTO updateEmployee(Long id, @Valid EmployeeDTO employeeDTO) {
        Employee existingEmployee = getEmployeeByIdUtil(id);

        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setSalary(employeeDTO.getSalary());

        log.info("Employee with ID {} updated", id);
        return EmployeeMapper.toDTO(employeeRepository.save(existingEmployee));
    }

    @Transactional
    @Override
    public void deleteEmployee(Long id) {
        Employee existingEmployee = getEmployeeByIdUtil(id);
        existingEmployee.setActive(false);
        employeeRepository.save(existingEmployee);
        log.info("Employee with ID {} deactivated", id);
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
		log.info("Employee with ID {} found", id);
		return employee;
	}

	@Transactional
	@Override
	public EmployeeDTO restoreEmployee(Long id) {
		Optional<Employee> employeeOptional = employeeRepository.findById(id);
		Employee existingEmployee = employeeOptional.orElseThrow(() -> new ResourceNotFoundException("Employee not found"));
		if(existingEmployee.isActive()) {
			throw new IllegalStateException("Employee is already active");
		}
		existingEmployee.setActive(true);
		Employee employee = employeeRepository.save(existingEmployee);
		log.info("Employee with ID {} restored", id);
		return EmployeeMapper.toDTO(employee);
	}
}
