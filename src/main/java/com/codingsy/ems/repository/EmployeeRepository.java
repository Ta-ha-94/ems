package com.codingsy.ems.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.codingsy.ems.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
	//JpaRepository<Employee, Long> provides all CRUD methods automatically!
	@Query("SELECT e FROM Employee e WHERE e.active = true")
	Page<Employee> findAllActive(Pageable pageable);
	
	//@Query() not needed because method structure is provided by JPA itself
	Optional<Employee> findByIdAndActiveTrue(Long id);
}
