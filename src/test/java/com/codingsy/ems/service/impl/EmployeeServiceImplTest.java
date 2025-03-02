package com.codingsy.ems.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.codingsy.ems.dto.EmployeeDTO;
import com.codingsy.ems.exception.ResourceNotFoundException;
import com.codingsy.ems.mapper.EmployeeMapper;
import com.codingsy.ems.model.Employee;
import com.codingsy.ems.repository.EmployeeRepository;
import com.codingsy.ems.service.AuditLogService;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {
	
	@Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmailService mailService;

    @Mock
    private AuditLogService auditLogService;
    
    @Mock
    private EmployeeMapper employeeMapper;

    @InjectMocks
    private EmployeeServiceImpl employeeService;
    
    private Employee createEmployee(Long id, String name, String email, Double salary, boolean active) {
        Employee emp = new Employee();
        emp.setId(id);
        emp.setName(name);
        emp.setEmail(email);
        emp.setSalary(salary);
        emp.setActive(active);
        return emp;
    }

    private EmployeeDTO createEmployeeDTO(Long id, String name, String email, Double salary) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(id);
        dto.setName(name);
        dto.setEmail(email);
        dto.setSalary(salary);
        return dto;
    }

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(employeeRepository, mailService, auditLogService);
    }
    
	@Test
    void saveEmployee_savesAndReturnsDTO() {
		// Arrange
        EmployeeDTO inputDTO = createEmployeeDTO(null, "John Doe", "john@example.com", 50000.0);
        Employee employee = createEmployee(null, "John Doe", "john@example.com", 50000.0, true);
        Employee savedEmployee = createEmployee(1L, "John Doe", "john@example.com", 50000.0, true);
        EmployeeDTO savedDTO = createEmployeeDTO(1L, "John Doe", "john@example.com", 50000.0);

        try (MockedStatic<EmployeeMapper> mockedMapper = Mockito.mockStatic(EmployeeMapper.class)) {
            mockedMapper.when(() -> EmployeeMapper.toEntity(inputDTO)).thenReturn(employee);
            mockedMapper.when(() -> EmployeeMapper.toDTO(savedEmployee)).thenReturn(savedDTO);

            when(employeeRepository.save(employee)).thenReturn(savedEmployee);

            // Act
            EmployeeDTO result = employeeService.saveEmployee(inputDTO);

            // Assert
            assertEquals(1L, result.getId());
            assertEquals("John Doe", result.getName());
            verify(mailService, times(1)).sendEmail("john@example.com", "John Doe");
            verify(auditLogService, times(1)).logAction("John Doe", "EMPLOYEE_CREATED", "CREATED EMPLOYEE WITH ID: 1");
        }
    }
	
	@Test
	void updateEmployee_updatesAndReturnsDTO() {
		//Arrange
		Long id = 1L;
		Employee existingData = createEmployee(id, "John Doe", "john@doe.com", 22000.00d, true);
		EmployeeDTO newData = createEmployeeDTO(id, "Jane Doe", "jane@doe.com", 15000.00d);
		Employee updatedEmployee = createEmployee(id, "Jane Doe", "jane@doe.com", 15000.00d, true);
		EmployeeDTO updatedEmployeeDTO = createEmployeeDTO(id, "Jane Doe", "jane@doe.com", 15000.00d);
		
		when(employeeRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(existingData));
		when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);
		
		try(MockedStatic<EmployeeMapper> mockedStatic = Mockito.mockStatic(EmployeeMapper.class)) {
			mockedStatic.when(() -> EmployeeMapper.toDTO(updatedEmployee)).thenReturn(updatedEmployeeDTO);
			
			//Act
			EmployeeDTO result = employeeService.updateEmployee(id, newData);
			
			//Assert
			assertEquals("Jane Doe", result.getName());
			assertEquals("jane@doe.com", result.getEmail());
			assertEquals(15000.00d, result.getSalary());
			verify(employeeRepository, times(1)).save(any(Employee.class));
	        verify(auditLogService, times(1)).logAction("Jane Doe", "EMPLOYEE_UPDATED", "UPDATED EMPLOYEE WITH ID: 1");
	        verifyNoInteractions(mailService); // No email sent in this method
		}
	}
	
	@Test
    void updateEmployee_throwsExceptionWhenEmployeeNotFound() {
        // Arrange
        Long id = 1L;
        EmployeeDTO updateDTO = createEmployeeDTO(id, "New Name", "new@example.com", 45000.0);

        when(employeeRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            employeeService.updateEmployee(id, updateDTO);
        });
        assertEquals("Active employee not found with id: 1", exception.getMessage());
        verify(employeeRepository, times(1)).findByIdAndActiveTrue(id);
        verifyNoInteractions(auditLogService, mailService);
    }
	
	@Test
	void deleteEmployee_deactivatesEmployee() {
		//Arrange
		Long id = 1L;
		Employee existingEmployee = createEmployee(id, "John Doe", "john@example.com", 50000.0, true);
		Employee deactivatedEmployee = createEmployee(id, "John Doe", "john@example.com", 50000.0, false);
		
		when(employeeRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(existingEmployee));
		when(employeeRepository.save(any(Employee.class))).thenReturn(deactivatedEmployee);
		
		//Act
		employeeService.deleteEmployee(id);
		
		verify(employeeRepository, times(1)).save(any(Employee.class));
		verify(auditLogService, times(1)).logAction("John Doe", "EMPLOYEE_DELETED", "Deleted employee with ID: 1".toUpperCase());
		verifyNoInteractions(mailService); // No email sent in this method
	}
	
	@Test
	void deleteEmployee_throwsExceptionWhenEmployeeNotFound() {
	    // Arrange
	    Long id = 1L;

	    when(employeeRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

	    // Act & Assert
	    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
	        employeeService.deleteEmployee(id);
	    });
	    assertEquals("Active employee not found with id: 1", exception.getMessage());
	    verifyNoInteractions(auditLogService, mailService);
	}
	
	@Test
	void getAllEmployees_returnsPagedDTOs() {
		//Arrange
		int page = 0;
	    int size = 10;
	    String sortBy = "name";
	    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
	    
		Employee emp1 = createEmployee(1L, "Alice Smith", "alice@example.com", 50000.0, true);
	    Employee emp2 = createEmployee(2L, "Bob Johnson", "bob@example.com", 60000.0, true);
	    
	    Page<Employee> employeePage = new PageImpl<> (List.of(emp1, emp2), pageable, 2);
	    
	    EmployeeDTO dto1 = createEmployeeDTO(1L, "Alice Smith", "alice@example.com", 50000.0);
	    EmployeeDTO dto2 = createEmployeeDTO(2L, "Bob Johnson", "bob@example.com", 60000.0);
	    
	    when(employeeRepository.findAll(pageable)).thenReturn(employeePage);
	    
	    try(MockedStatic<EmployeeMapper> mockedMapper = Mockito.mockStatic(EmployeeMapper.class)) {
	    	mockedMapper.when(() -> EmployeeMapper.toDTO(emp1)).thenReturn(dto1);
	        mockedMapper.when(() -> EmployeeMapper.toDTO(emp2)).thenReturn(dto2);
	        
	        // Act
	        Page<EmployeeDTO> result = employeeService.getAllEmployees(page, size, sortBy);
	        
	        assertEquals(2, result.getTotalElements());
	        assertEquals("Alice Smith", result.getContent().get(0).getName());
	        assertEquals("Bob Johnson", result.getContent().get(1).getName());
	        verify(employeeRepository, times(1)).findAll(pageable);
	        verifyNoInteractions(auditLogService, mailService);
	    }
	}
	
	@Test
	void getAllEmployees_returnsEmptyPage() {
	    // Arrange
	    int page = 0;
	    int size = 10;
	    String sortBy = "name";
	    Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
	    Page<Employee> emptyPage = new PageImpl<>(Collections.emptyList(), pageable, 0);

	    when(employeeRepository.findAll(pageable)).thenReturn(emptyPage);

	    // Act
	    Page<EmployeeDTO> result = employeeService.getAllEmployees(page, size, sortBy);

	    // Assert
	    assertEquals(0, result.getTotalElements());
	    verify(employeeRepository, times(1)).findAll(pageable);
	    verifyNoInteractions(auditLogService, mailService);
	}

	@Test
	void getEmployeeById_returnsDTO() {
	    // Arrange
	    Long id = 1L;
	    Employee employee = createEmployee(id, "Alice Smith", "alice@example.com", 50000.0, true);
	    EmployeeDTO dto = createEmployeeDTO(id, "Alice Smith", "alice@example.com", 50000.0);

	    when(employeeRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.of(employee));
	    try (MockedStatic<EmployeeMapper> mockedMapper = Mockito.mockStatic(EmployeeMapper.class)) {
	        mockedMapper.when(() -> EmployeeMapper.toDTO(employee)).thenReturn(dto);

	        // Act
	        EmployeeDTO result = employeeService.getEmployeeById(id);

	        // Assert
	        assertEquals("Alice Smith", result.getName());
	        verify(employeeRepository, times(1)).findByIdAndActiveTrue(id);
	        verifyNoInteractions(auditLogService, mailService);
	    }
	}

	@Test
	void getEmployeeById_throwsExceptionWhenNotFound() {
	    // Arrange
	    Long id = 1L;

	    when(employeeRepository.findByIdAndActiveTrue(id)).thenReturn(Optional.empty());

	    // Act & Assert
	    ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
	        employeeService.getEmployeeById(id);
	    });
	    assertEquals("Active employee not found with id: 1", exception.getMessage());
	    verify(employeeRepository, times(1)).findByIdAndActiveTrue(id);
	    verifyNoInteractions(auditLogService, mailService);
	}
	
//	@Test
//	void filterEmployee_returnsFilteredDTOs() {
//		// Arrange
//	    String name = "Alice";
//	    Double minSalary = 40000.0;
//	    Double maxSalary = 60000.0;
//	    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));
//
//	    Employee emp = createEmployee(1L, "Alice Smith", "alice@example.com", 50000.0, true);
//	    Page<Employee> employeePage = new PageImpl<>(List.of(emp), pageable, 1);
//	    EmployeeDTO dto = createEmployeeDTO(1L, "Alice Smith", "alice@example.com", 50000.0);
//
//	    Specification<Employee> spec = Specification
//	            .where(EmployeeSpecification.hasName(name))
//	            .and(EmployeeSpecification.hasMinSalary(minSalary))
//	            .and(EmployeeSpecification.hasMaxSalary(maxSalary));
//	    // Use type-safe any() instead of any(Specification.class)
//	    when(employeeRepository.findAll(any(), eq(pageable))).thenReturn(employeePage);
//	    try (MockedStatic<EmployeeMapper> mockedMapper = Mockito.mockStatic(EmployeeMapper.class)) {
//	        mockedMapper.when(() -> EmployeeMapper.toDTO(emp)).thenReturn(dto);
//
//	        // Act
//	        Page<EmployeeDTO> result = employeeService.filterEmployee(name, minSalary, maxSalary, pageable);
//
//	        // Assert
//	        assertEquals(1, result.getTotalElements());
//	        assertEquals("Alice Smith", result.getContent().get(0).getName());
//	        // Use type-safe any() in verify as well
//	        verify(employeeRepository, times(1)).findAll(any(), eq(pageable));
//	        verifyNoInteractions(auditLogService, mailService);
//	    }
//	}

	@Test
	void filterEmployee_throwsExceptionWhenMinSalaryGreaterThanMax() {
	    // Arrange
	    String name = "Alice";
	    Double minSalary = 60000.0;
	    Double maxSalary = 40000.0;
	    Pageable pageable = PageRequest.of(0, 10, Sort.by("name"));

	    // Act & Assert
	    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
	        employeeService.filterEmployee(name, minSalary, maxSalary, pageable);
	    });
	    assertEquals("minSalary cannot be greater than maxSalary", exception.getMessage());
	    verifyNoInteractions(employeeRepository, auditLogService, mailService);
	}
}
