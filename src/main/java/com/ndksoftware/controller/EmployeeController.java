package com.ndksoftware.controller;

import com.ndksoftware.SuccessResponse;
import com.ndksoftware.model.Employee;
import com.ndksoftware.service.EmployeeService;
import com.ndksoftware.exception.EmployeeNotFoundException;
import com.ndksoftware.exception.ValidationException;
import com.ndksoftware.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.BufferedReader;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet("/api/employees/*")
public class EmployeeController extends HttpServlet {
    private final EmployeeService employeeService;
    private final ObjectMapper objectMapper;

    public EmployeeController() {
        this.employeeService = new EmployeeService();
        this.objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Read request body
            String requestBody = readRequestBody(request);

            // Parse JSON to Employee object
            Employee employee = objectMapper.readValue(requestBody, Employee.class);

            // Create employee and get ID
            Long id = employeeService.createEmployee(employee);

            // Set response
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(),
                    new SuccessResponse("Employee created successfully", id));

        } catch (ValidationException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error creating employee: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String pathInfo = request.getPathInfo();
            String searchCriteria = request.getParameter("criteria");
            String searchValue = request.getParameter("value");

            List<Employee> employees;

            if (pathInfo == null || pathInfo.equals("/")) {
                if (searchCriteria != null && searchValue != null) {
                    // Search employees based on criteria
                    employees = employeeService.searchEmployees(searchCriteria, searchValue);
                } else {
                    // Get all employees
                    employees = employeeService.getAllEmployees();
                }

                response.setContentType("application/json");
                objectMapper.writeValue(response.getWriter(), employees);

            } else {
                // Get employee by ID
                Long id = parseEmployeeId(pathInfo);
                Employee employee = employeeService.getEmployee(id);

                response.setContentType("application/json");
                objectMapper.writeValue(response.getWriter(), employee);
            }

        } catch (EmployeeNotFoundException e) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (IllegalArgumentException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error retrieving employee(s): " + e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get employee ID from path
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Employee ID is required");
                return;
            }

            Long id = parseEmployeeId(pathInfo);

            // Read request body
            String requestBody = readRequestBody(request);

            // Parse JSON to Employee object
            Employee employee = objectMapper.readValue(requestBody, Employee.class);

            // Update employee
            employeeService.updateEmployee(id, employee);

            // Set response
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(),
                    new SuccessResponse("Employee updated successfully", id));

        } catch (EmployeeNotFoundException e) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (ValidationException e) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error updating employee: " + e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // Get employee ID from path
            String pathInfo = request.getPathInfo();
            if (pathInfo == null || pathInfo.equals("/")) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST, "Employee ID is required");
                return;
            }

            Long id = parseEmployeeId(pathInfo);

            // Delete employee
            employeeService.deleteEmployee(id);

            // Set response
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json");
            objectMapper.writeValue(response.getWriter(),
                    new SuccessResponse("Employee deleted successfully", id));

        } catch (EmployeeNotFoundException e) {
            sendError(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Error deleting employee: " + e.getMessage());
        }
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        BufferedReader reader = request.getReader();
        return reader.lines().collect(Collectors.joining());
    }

    private Long parseEmployeeId(String pathInfo) {
        try {
            return Long.parseLong(pathInfo.substring(1));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid employee ID");
        }
    }

    private void sendError(HttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        ErrorResponse errorResponse = new ErrorResponse(status, message);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}