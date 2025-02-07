package com.ndksoftware.dao;

import com.ndksoftware.model.Employee;
import java.time.LocalDate;
import java.util.List;

public interface EmployeeDAO {
    Long create(Employee employee);
    Employee findById(Long id);
    List<Employee> findAll();
    List<Employee> findByName(String name);
    List<Employee> findByDepartment(String department);
    List<Employee> findByPosition(String position);
    List<Employee> findByHireDate(LocalDate hireDate);
    void update(Employee employee);
    void delete(Long id);
    boolean exists(Long id);
}