package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.mapper.EmployeeDTOMapper;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.repository.EmployeeRepository;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private EmployeeDTOMapper employeeDTOMapper;

    @InjectMocks
    private EmployeeService employeeService;

    // Constants for test data
    private static final Long EMPLOYEE_NUMBER_1 = 10001L;
    private static final Long EMPLOYEE_NUMBER_2 = 10002L;
    private static final String USERNAME_1 = "john.doe";
    private static final String USERNAME_2 = "jane.smith";
    private static final String LAST_NAME_1 = "Doe";
    private static final String FIRST_NAME_1 = "John";
    private static final String BIRTHDATE_1 = "1990-01-01";
    private static final String ADDRESS_1 = "123 Main St";
    private static final String PHONE_NUMBER_1 = "1234567890";
    private static final String LAST_NAME_2 = "Smith";
    private static final String FIRST_NAME_2 = "Jane";
    private static final String BIRTHDATE_2 = "1991-02-02";
    private static final String ADDRESS_2 = "456 Oak Ave";
    private static final String PHONE_NUMBER_2 = "0987654321";

    private Employee employee1;
    private Employee employee2;
    private EmployeeDTO employeeDTO1;
    private EmployeeDTO employeeDTO2;
    private List<Employee> employees;

    /**
     * Initializes test data before each test case.
     */
    @BeforeEach
    void setUp() {
        employee1 = new Employee();
        employee1.setEmployeeNumber(EMPLOYEE_NUMBER_1);
        employee1.setUsername(USERNAME_1);
        employee1.setPersonalInfo(new PersonalInfo(LAST_NAME_1, FIRST_NAME_1, BIRTHDATE_1, ADDRESS_1, PHONE_NUMBER_1));

        employee2 = new Employee();
        employee2.setEmployeeNumber(EMPLOYEE_NUMBER_2);
        employee2.setUsername(USERNAME_2);
        employee2.setPersonalInfo(new PersonalInfo(LAST_NAME_2, FIRST_NAME_2, BIRTHDATE_2, ADDRESS_2, PHONE_NUMBER_2));

        employeeDTO1 = new EmployeeDTO(employee1);
        employeeDTO2 = new EmployeeDTO(employee2);

        employees = Arrays.asList(employee1, employee2);
    }

    /**
     * Stubs the mapper to return expected DTOs.
     */
    private void mockEmployeeDTOMapper() {
        when(employeeDTOMapper.apply(employee1)).thenReturn(employeeDTO1);
        when(employeeDTOMapper.apply(employee2)).thenReturn(employeeDTO2);
    }

    /**
     * Helper method to validate that two {@link EmployeeDTO} instances are equal.
     */
    private void assertEmployeeDTO(EmployeeDTO expected, EmployeeDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.employee().getEmployeeNumber(), actual.employee().getEmployeeNumber());
        assertEquals(expected.employee().getUsername(), actual.employee().getUsername());
        assertEquals(expected.employee().getPersonalInfo().getLastName(),
                actual.employee().getPersonalInfo().getLastName());
        assertEquals(expected.employee().getPersonalInfo().getFirstName(),
                actual.employee().getPersonalInfo().getFirstName());
        assertEquals(expected.employee().getPersonalInfo().getBirthday(),
                actual.employee().getPersonalInfo().getBirthday());
        assertEquals(expected.employee().getPersonalInfo().getAddress(),
                actual.employee().getPersonalInfo().getAddress());
        assertEquals(expected.employee().getPersonalInfo().getPhoneNumber(),
                actual.employee().getPersonalInfo().getPhoneNumber());
    }

    /**
     * Tests {@link EmployeeService#getAllEmployees()} for correct result.
     */
    @Test
    void testGetAllEmployees() {
        when(employeeRepository.findAll()).thenReturn(employees);
        mockEmployeeDTOMapper();

        List<EmployeeDTO> result = employeeService.getAllEmployees();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEmployeeDTO(employeeDTO1, result.get(0));
        assertEmployeeDTO(employeeDTO2, result.get(1));
    }

    /**
     * Tests {@link EmployeeService#getEmployeeById(Long)} for a valid employee ID.
     */
    @Test
    void testGetEmployeeById() {
        when(employeeRepository.findById(EMPLOYEE_NUMBER_1)).thenReturn(Optional.of(employee1));
        when(employeeDTOMapper.apply(employee1)).thenReturn(employeeDTO1);

        EmployeeDTO result = employeeService.getEmployeeById(EMPLOYEE_NUMBER_1);

        assertEmployeeDTO(employeeDTO1, result);
    }

    /**
     * Tests {@link EmployeeService#getEmployeeByEmployeeNum(Long)} using the
     * employee number.
     */
    @Test
    void testGetEmployeeByEmployeeNum() {
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER_1)).thenReturn(Optional.of(employee1));
        when(employeeDTOMapper.apply(employee1)).thenReturn(employeeDTO1);

        EmployeeDTO result = employeeService.getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_1);

        assertEmployeeDTO(employeeDTO1, result);
    }

    /**
     * Tests {@link EmployeeService#getAllEmployeeNums()} for retrieving employee
     * numbers only.
     */
    @Test
    void testGetAllEmployeeNums() {
        List<Long> expectedEmployeeNums = Arrays.asList(EMPLOYEE_NUMBER_1, EMPLOYEE_NUMBER_2);

        when(employeeRepository.findAllEmployeeNumbers()).thenReturn(expectedEmployeeNums);

        List<Long> result = employeeService.getAllEmployeeNums();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedEmployeeNums.get(0), result.get(0));
        assertEquals(expectedEmployeeNums.get(1), result.get(1));
    }
}
