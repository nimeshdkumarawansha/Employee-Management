package com.ndksoftware.model;

import lombok.Data;
import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Position is required")
    @Column(nullable = false)
    private String position;

    @NotBlank(message = "Department is required")
    @Column(nullable = false)
    private String department;

    @NotNull(message = "Hire date is required")
    @Column(name = "hire_date", nullable = false)
    private LocalDate hireDate;

    @NotNull(message = "Salary is required")
    @Min(value = 0, message = "Salary must be positive")
    private Double salary;

    @Column(name = "created_at")
    private LocalDate createdAt;

    @Column(name = "updated_at")
    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDate.now();
        updatedAt = LocalDate.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDate.now();
    }
}