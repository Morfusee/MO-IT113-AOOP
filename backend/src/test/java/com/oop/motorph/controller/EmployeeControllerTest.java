package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.service.EmployeeService;
import com.oop.motorph.utils.ApiResponse;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    private EmployeeDTO mockEmployee;
    private List<EmployeeDTO> mockEmployeeList;

    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final String USERNAME = "john.doe";
    private static final String LAST_NAME = "Doe";
    private static final String FIRST_NAME = "John";
    private static final String BIRTHDATE = "1990-01-01";
    private static final String ADDRESS = "123 Main St";
    private static final String PHONE_NUMBER = "1234567890";

    @BeforeEach
    void setup() {
        Employee newEmployee = new Employee();
        newEmployee.setEmployeeNumber(EMPLOYEE_NUMBER);
        newEmployee.setUsername(USERNAME);
        newEmployee.setPersonalInfo(new PersonalInfo(LAST_NAME, FIRST_NAME, BIRTHDATE, ADDRESS, PHONE_NUMBER));

        mockEmployee = new EmployeeDTO(newEmployee);
        mockEmployeeList = Arrays.asList(mockEmployee);
    }

    /**
     * Helper method for asserting that a response returns 200 OK and contains the expected message.
     */
    @SuppressWarnings("null")
    private void assertOkResponse(ResponseEntity<ApiResponse<?>> response, String expectedMessage) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody() != null, "Response body should not be null");
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    /**
     * Tests fetching all employees successfully returns a list and the correct response message.
     */
    @Test
    void testGetAllEmployees_Success() {
        when(employeeService.getAllEmployees()).thenReturn(mockEmployeeList);

        ResponseEntity<ApiResponse<?>> response = employeeController.getAllEmployees();

        assertOkResponse(response, "Employees fetched successfully.");
    }

    /**
     * Tests fetching a single employee by employee number returns the correct employee and response message.
     */
    @Test
    void testGetEmployeeByEmployeeNum_Success() {
        when(employeeService.getEmployeeByEmployeeNum(10001L)).thenReturn(mockEmployee);

        ResponseEntity<ApiResponse<?>> response = employeeController.getEmployeeByEmployeeNum(10001L);

        assertOkResponse(response, "Employee fetched successfully.");
    }
}
