package com.codingsy.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeDTO {
	private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Salary must not be null")
    @Min(value = 10000, message = "Salary must be at least 10,000")
    private Double salary;

    // Constructors
    public EmployeeDTO() {}

    public EmployeeDTO(Long id, String name, String email, Double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", name=" + name + ", email=" + email + ", salary=" + salary + "]";
	}
    
}
