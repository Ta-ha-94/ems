package com.codingsy.ems.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.codingsy.ems.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	//JpaRepository<Employee, Long> provides all CRUD methods automatically!
	Page<Employee> findAll(Pageable pageable);
}
