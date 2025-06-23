package com.oop.motorph.controller;

import java.util.List;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.service.EmployeeService;
import com.oop.motorph.service.HRManagerService;
import com.oop.motorph.utils.ApiResponse;

/**
 * REST controller for Human Resources (HR) to manage employee records.
 * This controller provides endpoints for HR personnel to perform CRUD
 * operations on employee data.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/employees")
public class HREmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HRManagerService hrManagerService;

    /**
     * Retrieves a list of all employees.
     * This endpoint is typically used by HR to get an overview of all employee
     * records.
     *
     * @return A ResponseEntity containing an ApiResponse with a list of
     *         EmployeeDTOs or an error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = hrManagerService.getAllEmployees();
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Employees fetched successfully.",
                    employees));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch employees.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Retrieves a single employee by their employee number.
     * This endpoint allows HR to view detailed information about a specific
     * employee.
     *
     * @param employeeNum The employee number to retrieve.
     * @return A ResponseEntity containing an ApiResponse with an EmployeeDTO or an
     *         error message.
     */
    @GetMapping("/{employeeNum}")
    public ResponseEntity<ApiResponse<?>> getEmployeeById(@PathVariable Long employeeNum) {
        try {
            EmployeeDTO fetchedEmployee = employeeService.getEmployeeById(employeeNum);
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Employee fetched successfully.",
                    fetchedEmployee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch employee.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Creates a new employee record.
     * This endpoint is used by HR to add new employees to the system.
     *
     * @param employeeRequest The EmployeeRequestDTO containing the data for the new
     *                        employee.
     * @return A ResponseEntity containing an ApiResponse with the created
     *         EmployeeDTO or an error message.
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createEmployee(@RequestBody EmployeeRequestDTO employeeRequest) {
        try {
            EmployeeDTO createdEmployee = hrManagerService.createEmployee(employeeRequest);
            return ResponseEntity.ok().body(ApiResponse.created("Employee created successfully.", createdEmployee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to create employee.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Updates an existing employee record.
     * This endpoint allows HR to modify details of an employee.
     *
     * @param employeeNum     The employee number of the employee to update.
     * @param employeeRequest The EmployeeRequestDTO containing the updated employee
     *                        data.
     * @return A ResponseEntity containing an ApiResponse with the updated
     *         EmployeeDTO or an error message.
     */
    @PatchMapping("/{employeeNum}")
    public ResponseEntity<ApiResponse<?>> updateEmployee(@PathVariable Long employeeNum,
            @RequestBody EmployeeRequestDTO employeeRequest) {
        try {
            EmployeeDTO updatedEmployee = hrManagerService.updateEmployee(employeeNum, employeeRequest);
            return ResponseEntity.ok().body(
                    ApiResponse.success("Employee updated successfully.", updatedEmployee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to update employee.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Deletes an employee record by their employee number.
     * This endpoint is used by HR to remove an employee from the system.
     *
     * @param employeeNum The employee number of the employee to delete.
     * @return A ResponseEntity containing an ApiResponse with the deleted
     *         EmployeeDTO or an error message.
     */
    @DeleteMapping("/{employeeNum}")
    public ResponseEntity<ApiResponse<?>> deleteEmployee(@PathVariable Long employeeNum) {
        try {
            EmployeeDTO deletedEmployee = hrManagerService.deleteEmployeeById(employeeNum);
            return ResponseEntity.ok().body(
                    ApiResponse.success("Employee deleted successfully.", deletedEmployee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to delete employee.",
                    Map.of("error", e.getMessage())));
        }
    }
}