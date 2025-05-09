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

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/employees")
public class HREmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private HRManagerService hrManagerService;

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
