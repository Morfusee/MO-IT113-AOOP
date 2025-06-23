package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.entity.User;
import com.oop.motorph.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private UserService userService;

    // Constants for test data
    private static final Long USER_ID = 1L;
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Long INVALID_EMPLOYEE_NUMBER = 99999L;
    private static final String USERNAME = "testuser";
    private static final String PASSWORD = "testpass";
    private static final String USERNAME_WITH_WHITESPACE = " testuser ";
    private static final String PASSWORD_WITH_WHITESPACE = " testpass ";
    private static final String WRONG_USERNAME = "wronguser";
    private static final String WRONG_PASSWORD = "wrongpass";
    private static final String LAST_NAME = "Doe";
    private static final String FIRST_NAME = "John";
    private static final String BIRTHDATE = "1990-01-01";
    private static final String ADDRESS = "123 Street";
    private static final String PHONE_NUMBER = "1234567890";

    private User user;
    private Employee employee;
    private EmployeeDTO employeeDTO;

    @BeforeEach
    void setUp() {
        user = new Employee(); // Assuming User can be an Employee for this context
        user.setUserId(USER_ID);
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);

        employee = new Employee();
        employee.setEmployeeNumber(EMPLOYEE_NUMBER);
        employee.setUserId(USER_ID);
        employee.setUsername(USERNAME);
        employee.setPassword(PASSWORD);
        employee.setPersonalInfo(new PersonalInfo(LAST_NAME, FIRST_NAME, BIRTHDATE, ADDRESS, PHONE_NUMBER));

        employeeDTO = new EmployeeDTO(employee);
    }

    /**
     * Verifies that the returned EmployeeDTO matches the expected properties.
     */
    private void assertEmployeeDTOProperties(EmployeeDTO expected, EmployeeDTO actual) {
        assertNotNull(actual);
        assertNotNull(actual.employee());
        assertEquals(expected.employee().getEmployeeNumber(), actual.employee().getEmployeeNumber());
        assertEquals(expected.employee().getUsername(), actual.employee().getUsername());
        assertEquals(expected.employee().getUserId(), actual.employee().getUserId());
        assertEquals(expected.employee().getPassword(), actual.employee().getPassword());
        assertEquals(expected.employee().getPersonalInfo().getLastName(),
                actual.employee().getPersonalInfo().getLastName());
        assertEquals(expected.employee().getPersonalInfo().getFirstName(),
                actual.employee().getPersonalInfo().getFirstName());
    }

    /**
     * Tests successful login with correct credentials.
     */
    @Test
    void testLoginUser_Success() {
        when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(user);
        when(employeeService.getEmployeeById(USER_ID)).thenReturn(employeeDTO);

        EmployeeDTO result = userService.loginUser(USERNAME, PASSWORD);

        assertEmployeeDTOProperties(employeeDTO, result);
    }

    /**
     * Tests login where username and password contain extra whitespace.
     */
    @Test
    void testLoginUser_WithWhitespace() {
        when(userRepository.findByUsernameAndPassword(USERNAME, PASSWORD)).thenReturn(user);
        when(employeeService.getEmployeeById(USER_ID)).thenReturn(employeeDTO);

        EmployeeDTO result = userService.loginUser(USERNAME_WITH_WHITESPACE, PASSWORD_WITH_WHITESPACE);

        assertEmployeeDTOProperties(employeeDTO, result);
    }

    /**
     * Tests login attempt with incorrect credentials.
     */
    @Test
    void testLoginUser_InvalidCredentials() {
        when(userRepository.findByUsernameAndPassword(WRONG_USERNAME, WRONG_PASSWORD)).thenReturn(null);

        EmployeeDTO result = userService.loginUser(WRONG_USERNAME, WRONG_PASSWORD);

        assertNull(result);
    }

    /**
     * Tests successful authentication using a valid employee number.
     */
    @Test
    void testAuthenticateUser_Success() {
        when(employeeService.getEmployeeByEmployeeNum(EMPLOYEE_NUMBER)).thenReturn(employeeDTO);

        EmployeeDTO result = userService.authenticateUser(EMPLOYEE_NUMBER);

        assertEmployeeDTOProperties(employeeDTO, result);
    }

    /**
     * Tests authentication failure when the employee number is not found.
     */
    @Test
    void testAuthenticateUser_InvalidEmployee() {
        when(employeeService.getEmployeeByEmployeeNum(INVALID_EMPLOYEE_NUMBER)).thenReturn(null);

        EmployeeDTO result = userService.authenticateUser(INVALID_EMPLOYEE_NUMBER);

        assertNull(result);
    }

    /**
     * Tests authentication with a null employee number.
     */
    @Test
    void testAuthenticateUser_NullEmployeeNum() {
        EmployeeDTO result = userService.authenticateUser(null);

        assertNull(result);
    }
}
