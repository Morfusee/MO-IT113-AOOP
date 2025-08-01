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
import com.oop.motorph.utils.ApiResponse;

/**
 * REST controller for managing employee-related operations.
 * Handles API requests for fetching, adding, updating, and deleting employee
 * records.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * Retrieves a list of all employees.
     *
     * @return A ResponseEntity containing an ApiResponse with a list of
     *         EmployeeDTOs or an error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllEmployees() {
        try {
            List<EmployeeDTO> employees = employeeService.getAllEmployees();
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
     *
     * @param employeeNum The employee number to retrieve.
     * @return A ResponseEntity containing an ApiResponse with an EmployeeDTO or an
     *         error message.
     */
    @GetMapping("/{employeeNum}")
    public ResponseEntity<ApiResponse<?>> getEmployeeByEmployeeNum(@PathVariable Long employeeNum) {
        try {
            EmployeeDTO fetchedEmployee = employeeService.getEmployeeByEmployeeNum(employeeNum);
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
}