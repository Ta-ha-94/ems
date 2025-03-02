package com.codingsy.ems.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * This class represents a Data Transfer Object (DTO) for the Employee entity.
 * It is used to transfer employee data between layers of the application.
 * 
 * @author Taha
 * @version 1.0
 */
public class EmployeeDTO {
	private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Salary must not be null")
    @Min(value = 10000, message = "Salary must be at least 10,000")
    private Double salary;

    /**
     * Default constructor.
     */
    public EmployeeDTO() {}

    /**
     * Parameterized constructor to initialize an EmployeeDTO object.
     *
     * @param id The ID of the employee.
     * @param name The name of the employee.
     * @param email The email of the employee.
     * @param salary The salary of the employee.
     */
    public EmployeeDTO(Long id, String name, String email, Double salary) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.salary = salary;
    }

    /**
     * Gets the ID of the employee.
     *
     * @return The ID of the employee.
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the ID of the employee.
     *
     * @param id The ID of the employee.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gets the name of the employee.
     *
     * @return The name of the employee.
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the employee.
     *
     * @param name The name of the employee.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the email of the employee.
     *
     * @return The email of the employee.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the employee.
     *
     * @param email The email of the employee.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gets the salary of the employee.
     *
     * @return The salary of the employee.
     */
    public Double getSalary() {
        return salary;
    }

    /**
     * Sets the salary of the employee.
     *
     * @param salary The salary of the employee.
     */
    public void setSalary(Double salary) {
        this.salary = salary;
    }

    /**
     * Returns a string representation of the EmployeeDTO object.
     *
     * @return A string representation of the EmployeeDTO object.
     */	@Override
	public String toString() {
		return "EmployeeDTO [id=" + id + ", name=" + name + ", email=" + email + ", salary=" + salary + "]";
	}
}
