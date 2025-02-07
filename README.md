# Employee Management System - Project Report

## Table of Contents
1. [Project Overview](#project-overview)
2. [Technical Stack](#technical-stack)
3. [Setup Instructions](#setup-instructions)
4. [Database Configuration](#database-configuration)
5. [API Documentation](#api-documentation)
6. [Error Handling](#error-handling)
7. [Testing Guide](#testing-guide)

## Project Overview

The Employee Management System is a RESTful web application built using the MVC architecture and Hibernate framework. It provides CRUD operations for managing employee data, including features such as:
- Creating new employee records
- Retrieving employee information
- Updating existing employee details
- Deleting employee records
- Searching employees by various criteria

## Technical Stack

- Java 11
- Maven 3.6+
- Hibernate 5.6.5.Final
- MySQL 8.0
- Servlet API 4.0
- Jackson (JSON processing)
- JUnit 4.13.2
- Lombok

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/nimeshdkumarawansha/Employee-Management
   cd employee-management-system
   ```

2. **Build the Project**
   ```bash
   mvn clean install
   ```

3. **Deploy to Application Server**
   - Copy the generated WAR file from `target/employee-management-system.war` to your application server's deployment directory
   - For Tomcat: Copy to `{TOMCAT_HOME}/webapps/`

4. **Start the Application Server**
   - For Tomcat: Run `{TOMCAT_HOME}/bin/startup.sh` (Linux/Mac) or `{TOMCAT_HOME}/bin/startup.bat` (Windows)

## Database Configuration

1. **Create MySQL Database**
   ```sql
   CREATE DATABASE employee_db;
   ```

2. **Configure Database Connection**
   - Update `src/main/resources/hibernate.cfg.xml` with your database credentials:
   ```xml
   <property name="hibernate.connection.url">jdbc:mysql://localhost:3306/employee_db</property>
   <property name="hibernate.connection.username">your_username</property>
   <property name="hibernate.connection.password">your_password</property>
   ```

## API Documentation

### 1. Create Employee
- **Endpoint:** `POST /api/employees`
- **Content-Type:** `application/json`
- **Request Body:**
  ```json
  {
    "name": "John Doe",
    "position": "Software Engineer",
    "department": "Engineering",
    "hireDate": "2024-02-07",
    "salary": 75000.00
  }
  ```
- **Success Response:**
  - Status: 201 Created
  - Body: `{ "id": 1 }`
- **Error Response:**
  - Status: 400 Bad Request (validation error)
  - Status: 500 Internal Server Error

### 2. Get All Employees
- **Endpoint:** `GET /api/employees`
- **Success Response:**
  - Status: 200 OK
  - Body: Array of employee objects
  ```json
  [
    {
      "id": 1,
      "name": "John Doe",
      "position": "Software Engineer",
      "department": "Engineering",
      "hireDate": "2024-02-07",
      "salary": 75000.00
    }
  ]
  ```

### 3. Get Employee by ID
- **Endpoint:** `GET /api/employees/{id}`
- **Success Response:**
  - Status: 200 OK
  - Body: Employee object
- **Error Response:**
  - Status: 404 Not Found
  - Status: 400 Bad Request (invalid ID)

### 4. Search Employees
- **Endpoint:** `GET /api/employees?criteria={criteria}&value={value}`
- **Parameters:**
  - criteria: name, department, position, or hireDate
  - value: search value
- **Example:** `/api/employees?criteria=department&value=Engineering`
- **Success Response:**
  - Status: 200 OK
  - Body: Array of matching employee objects

### 5. Update Employee
- **Endpoint:** `PUT /api/employees/{id}`
- **Content-Type:** `application/json`
- **Request Body:** Employee object
- **Success Response:**
  - Status: 204 No Content
- **Error Response:**
  - Status: 404 Not Found
  - Status: 400 Bad Request (validation error)

### 6. Delete Employee
- **Endpoint:** `DELETE /api/employees/{id}`
- **Success Response:**
  - Status: 204 No Content
- **Error Response:**
  - Status: 404 Not Found
  - Status: 400 Bad Request (invalid ID)

## Error Handling

The application implements comprehensive error handling with appropriate HTTP status codes and error messages:

1. **Validation Errors (400 Bad Request)**
   ```json
   {
     "status": 400,
     "message": "Validation failed: Name is required",
     "timestamp": 1707307200000
   }
   ```

2. **Resource Not Found (404 Not Found)**
   ```json
   {
     "status": 404,
     "message": "Employee not found with ID: 1",
     "timestamp": 1707307200000
   }
   ```

3. **Server Errors (500 Internal Server Error)**
   ```json
   {
     "status": 500,
     "message": "Internal server error occurred",
     "timestamp": 1707307200000
   }
   ```

## Testing Guide

### Using cURL

1. **Create Employee:**
   ```bash
   curl -X POST http://localhost:8080/employee-management/api/employees \
   -H "Content-Type: application/json" \
   -d '{
       "name": "John Doe",
       "position": "Software Engineer",
       "department": "Engineering",
       "hireDate": "2024-02-07",
       "salary": 75000.00
   }'
   ```

2. **Get All Employees:**
   ```bash
   curl http://localhost:8080/employee-management/api/employees
   ```

3. **Get Employee by ID:**
   ```bash
   curl http://localhost:8080/employee-management/api/employees/1
   ```

4. **Search Employees:**
   ```bash
   curl "http://localhost:8080/employee-management/api/employees?criteria=department&value=Engineering"
   ```

5. **Update Employee:**
   ```bash
   curl -X PUT http://localhost:8080/employee-management/api/employees/1 \
   -H "Content-Type: application/json" \
   -d '{
       "name": "John Doe",
       "position": "Senior Software Engineer",
       "department": "Engineering",
       "hireDate": "2024-02-07",
       "salary": 85000.00
   }'
   ```

6. **Delete Employee:**
   ```bash
   curl -X DELETE http://localhost:8080/employee-management/api/employees/1
   ```

### Using Postman

1. Import the provided Postman collection file
2. Set the base URL environment variable to your server address
3. Execute the requests in the collection

### Running Unit Tests

```bash
mvn test
```

This will execute all unit tests and generate a test report in the `target/surefire-reports` directory.
