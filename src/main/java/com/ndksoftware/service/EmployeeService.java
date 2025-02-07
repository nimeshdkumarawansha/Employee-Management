package com.ndksoftware.service;

import com.ndksoftware.dao.EmployeeDAO;
import com.ndksoftware.dao.EmployeeDAOImpl;
import com.ndksoftware.exception.EmployeeNotFoundException;
import com.ndksoftware.exception.ValidationException;
import com.ndksoftware.model.Employee;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class EmployeeService {
    private final EmployeeDAO employeeDAO;
    private final Validator validator;

    public EmployeeService() {
        this.employeeDAO = new EmployeeDAOImpl();
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    public Long createEmployee(Employee employee) throws ValidationException {
        validateEmployee(employee);
        return employeeDAO.create(employee);
    }

    public Employee getEmployee(Long id) throws EmployeeNotFoundException {
        Employee employee = employeeDAO.findById(id);
        if (employee == null) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        return employee;
    }

    public List<Employee> getAllEmployees() {
        return employeeDAO.findAll();
    }

    public List<Employee> searchEmployees(String criteria, String value) {
        switch (criteria.toLowerCase()) {
            case "name":
                return employeeDAO.findByName(value);
            case "department":
                return employeeDAO.findByDepartment(value);
            case "position":
                return employeeDAO.findByPosition(value);
            case "hiredate":
                return employeeDAO.findByHireDate(LocalDate.parse(value));
            default:
                throw new IllegalArgumentException("Invalid search criteria: " + criteria);
        }
    }

    public void updateEmployee(Long id, Employee employee) throws EmployeeNotFoundException, ValidationException {
        if (!employeeDAO.exists(id)) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        validateEmployee(employee);
        employee.setId(id);
        employeeDAO.update(employee);
    }

    public void deleteEmployee(Long id) throws EmployeeNotFoundException {
        if (!employeeDAO.exists(id)) {
            throw new EmployeeNotFoundException("Employee not found with ID: " + id);
        }
        employeeDAO.delete(id);
    }

    private void validateEmployee(Employee employee) throws ValidationException {
        Set<ConstraintViolation<Employee>> violations = validator.validate(employee);
        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (ConstraintViolation<Employee> violation : violations) {
                sb.append(violation.getMessage()).append("; ");
            }
            throw new ValidationException(sb.toString());
        }
    }
}