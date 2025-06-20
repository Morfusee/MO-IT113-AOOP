package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.dto.UserDTO;
import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.GovernmentIds;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.service.EmployeeService;
import com.oop.motorph.service.HRManagerService;
import com.oop.motorph.utils.ApiResponse;
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

@ExtendWith(MockitoExtension.class)
public class HREmployeeControllerTest {

    @InjectMocks
    private HREmployeeController hrEmployeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private HRManagerService hrManagerService;

    private Employee mockEmployee;
    private EmployeeDTO mockEmployeeDTO;
    private List<EmployeeDTO> mockEmployeeList;
    private EmployeeRequestDTO mockEmployeeRequest;
    private UserDTO userDTO;
    private PersonalInfo personalInfo;
    private EmploymentInfo employmentInfo;
    private GovernmentIds governmentIds;
    private Compensation compensation;

    // Test constants
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final String USERNAME = "johndoe";
    private static final String LAST_NAME = "Doe";
    private static final String FIRST_NAME = "John";
    private static final String BIRTHDATE = "1990-01-01";
    private static final String ADDRESS = "123 Street";
    private static final String PHONE_NUMBER = "1234567890";
    private static final String EMPLOYMENT_STATUS = "Regular";
    private static final String POSITION = "Developer";
    private static final String DEPARTMENT = "Manager";
    private static final String SSS_ID = "SSS123";
    private static final String TIN_ID = "TIN123";
    private static final String PAGIBIG_ID = "PAGIBIG123";
    private static final String PHILHEALTH_ID = "PHILHEALTH123";
    private static final Double BASIC_SALARY = 50000.0;
    private static final Double RICE_SUBSIDY = 1000.0;
    private static final Double PHONE_ALLOWANCE = 1000.0;
    private static final Double CLOTHING_ALLOWANCE = 1000.0;
    private static final Double GROSS_SEMI_MONTHLY_RATE = 1000.0;
    private static final Double HOURLY_RATE = 1000.0;

    @BeforeEach
    void setup() {
        personalInfo = new PersonalInfo(LAST_NAME, FIRST_NAME, BIRTHDATE, ADDRESS, PHONE_NUMBER);
        employmentInfo = new EmploymentInfo(EMPLOYMENT_STATUS, POSITION, DEPARTMENT);
        governmentIds = new GovernmentIds(SSS_ID, TIN_ID, PAGIBIG_ID, PHILHEALTH_ID);
        compensation = new Compensation(
                null,
                BASIC_SALARY,
                RICE_SUBSIDY,
                PHONE_ALLOWANCE,
                CLOTHING_ALLOWANCE,
                GROSS_SEMI_MONTHLY_RATE,
                HOURLY_RATE);

        mockEmployee = new Employee();
        mockEmployee.setEmployeeNumber(EMPLOYEE_NUMBER);
        mockEmployee.setUsername(USERNAME);
        mockEmployee.setPersonalInfo(personalInfo);
        mockEmployee.setEmploymentInfo(employmentInfo);
        mockEmployee.setGovernmentIds(governmentIds);
        mockEmployee.setCompensation(compensation);

        // Initialize mock employee
        mockEmployeeDTO = new EmployeeDTO(mockEmployee);
        userDTO = new UserDTO(USERNAME);
        mockEmployeeList = Arrays.asList(mockEmployeeDTO);
        mockEmployeeRequest = new EmployeeRequestDTO(
                EMPLOYEE_NUMBER, userDTO, personalInfo, employmentInfo, governmentIds, compensation);
    }

    private void assertOkResponse(ResponseEntity<ApiResponse<?>> response, String expectedMessage) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody() != null, "Response body should not be null");
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    /**
     * Test: Successfully retrieve all employees
     */
    @Test
    void testGetAllEmployees_Success() {
        when(hrManagerService.getAllEmployees()).thenReturn(mockEmployeeList);

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.getAllEmployees();

        assertOkResponse(response, "Employees fetched successfully.");
    }

    /**
     * Test: Handle failure when retrieving all employees
     */
    @Test
    void testGetAllEmployees_Error() {
        when(hrManagerService.getAllEmployees()).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.getAllEmployees();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully fetch an employee by ID
     */
    @Test
    void testGetEmployeeById_Success() {
        when(employeeService.getEmployeeById(anyLong())).thenReturn(mockEmployeeDTO);

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.getEmployeeById(EMPLOYEE_NUMBER);

        assertOkResponse(response, "Employee fetched successfully.");
    }

    /**
     * Test: Handle error when employee not found by ID
     */
    @Test
    void testGetEmployeeById_Error() {
        when(employeeService.getEmployeeById(anyLong())).thenThrow(new RuntimeException("Employee not found"));

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.getEmployeeById(EMPLOYEE_NUMBER);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully create a new employee
     */
    @Test
    void testCreateEmployee_Success() {
        when(hrManagerService.createEmployee(any(EmployeeRequestDTO.class))).thenReturn(mockEmployeeDTO);

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.createEmployee(mockEmployeeRequest);

        assertOkResponse(response, "Employee created successfully.");
    }

    /**
     * Test: Handle error when creating employee with invalid data
     */
    @Test
    void testCreateEmployee_Error() {
        when(hrManagerService.createEmployee(any(EmployeeRequestDTO.class)))
                .thenThrow(new RuntimeException("Invalid employee data"));

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.createEmployee(mockEmployeeRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully update an existing employee
     */
    @Test
    void testUpdateEmployee_Success() {
        when(hrManagerService.updateEmployee(anyLong(), any(EmployeeRequestDTO.class))).thenReturn(mockEmployeeDTO);

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.updateEmployee(EMPLOYEE_NUMBER,
                mockEmployeeRequest);

        assertOkResponse(response, "Employee updated successfully.");
    }

    /**
     * Test: Handle error when updating a non-existent employee
     */
    @Test
    void testUpdateEmployee_Error() {
        when(hrManagerService.updateEmployee(anyLong(), any(EmployeeRequestDTO.class)))
                .thenThrow(new RuntimeException("Employee not found"));

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.updateEmployee(EMPLOYEE_NUMBER,
                mockEmployeeRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully delete an employee by ID
     */
    @Test
    void testDeleteEmployee_Success() {
        when(hrManagerService.deleteEmployeeById(anyLong())).thenReturn(mockEmployeeDTO);

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.deleteEmployee(EMPLOYEE_NUMBER);

        assertOkResponse(response, "Employee deleted successfully.");
    }

    /**
     * Test: Handle error when trying to delete a non-existent employee
     */
    @Test
    void testDeleteEmployee_Error() {
        when(hrManagerService.deleteEmployeeById(anyLong())).thenThrow(new RuntimeException("Employee not found"));

        ResponseEntity<ApiResponse<?>> response = hrEmployeeController.deleteEmployee(EMPLOYEE_NUMBER);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}