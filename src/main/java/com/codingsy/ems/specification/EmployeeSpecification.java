package com.codingsy.ems.specification;

import org.springframework.data.jpa.domain.Specification;

import com.codingsy.ems.model.Employee;

//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;

public class EmployeeSpecification {
	public static Specification<Employee> hasName(String name){
		
//		Uses LIKE operator (%name%) for partial matching.
//		Converts both database and input name to lower case to make the query case-insensitive.
		
//		Equivalent SQL Query:
//		SELECT * FROM employees WHERE LOWER(name) LIKE '%name%';
		
//		Anonymous inner class implementation
//		Specification<Employee> hasNameSpecification = new Specification<Employee>() {
//
//			@Override
//			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				if(name == null)
//					return null;
//				else
//					return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
//			}
//		};
		
//		Lambda expression
		return (root, query, cb) -> name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
	}
	
	public static Specification<Employee> hasMinSalary(Double minSalary){
//		If minSalary is null, it returns null (i.e., no filter is applied).
//		Otherwise, it uses cb.greaterThanOrEqualTo() to filter employees where salary is at least minSalary.
		
//		Equivalent SQL Query:
//		SELECT * FROM employees WHERE salary >= minSalaray;
		
//		Anonymous inner class implementation
//		Specification<Employee> minSalarySpecification = new Specification<Employee>() {
//
//			@Override
//			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				if(minSalaray == null) return null;
//				return criteriaBuilder.greaterThanOrEqualTo(root.get("salary"), minSalaray);
//			}
//		};
//		
//		return minSalarySpecification;
		
//		Lambda expression
		return (root, query, cb) ->  minSalary == null ? null : cb.greaterThanOrEqualTo(root.get("salary"), minSalary);
	}
	
	public static Specification<Employee> hasMaxSalary(Double maxSalary){
		
//		If maxSalary is null, it returns null (i.e., no filter is applied).
//		Otherwise, it uses cb.lessThanOrEqualTo() to filter employees where salary is at most maxSalary.
		
//		Equivalent SQL Query:
//		SELECT * FROM employees WHERE salary <= maxSalary;
		
//		Anonymous inner class implementation
//		Specification<Employee> hasMaxSalarySpecification = new Specification<Employee>() {
//
//			@Override
//			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//				if(maxSalary == null) return null;
//				return criteriaBuilder.lessThanOrEqualTo(root.get("salary"), maxSalary);
//			}
//		};
//		
//		return hasMaxSalarySpecification;
		
//		Lambda expression
		return (r, q, c) -> maxSalary == null ? null : c.lessThanOrEqualTo(r.get("salary"), maxSalary);
	}
}
