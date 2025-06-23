package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.service.UserService;
import com.oop.motorph.utils.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;

    private MockHttpServletResponse mockResponse;
    private EmployeeDTO mockEmployeeDTO;

    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "password123";
    private static final String LAST_NAME = "Doe";
    private static final String FIRST_NAME = "John";
    private static final String BIRTHDATE = "1990-01-01";
    private static final String ADDRESS = "123 Main St";
    private static final String PHONE_NUMBER = "1234567890";

    @BeforeEach
    void setup() {
        mockResponse = new MockHttpServletResponse();

        Employee mockEmployee = new Employee();
        mockEmployee.setEmployeeNumber(EMPLOYEE_NUMBER);
        mockEmployee.setUsername(USERNAME);
        mockEmployee.setPersonalInfo(new PersonalInfo(
                LAST_NAME, FIRST_NAME, BIRTHDATE, ADDRESS, PHONE_NUMBER));

        mockEmployeeDTO = new EmployeeDTO(mockEmployee);
    }

    /**
     * Test: Successful login with valid credentials
     */
    @Test
    void testLoginUser_Success() {
        Map<String, String> loginRequest = Map.of(
                "username", USERNAME,
                "password", PASSWORD);
        when(userService.loginUser(anyString(), anyString())).thenReturn(mockEmployeeDTO);

        ResponseEntity<ApiResponse<?>> response = userController.loginUser(loginRequest, mockResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Login successful.", response.getBody().getMessage());
    }

    /**
     * Test: Login attempt with missing credentials (empty request map)
     */
    @Test
    void testLoginUser_MissingCredentials() {
        Map<String, String> loginRequest = Map.of();

        ResponseEntity<ApiResponse<?>> response = userController.loginUser(loginRequest, mockResponse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login failed.", response.getBody().getMessage());
    }

    /**
     * Test: Login attempt with invalid credentials
     */
    @Test
    void testLoginUser_InvalidCredentials() {
        Map<String, String> loginRequest = Map.of(
                "username", "invalid",
                "password", "invalid");
        when(userService.loginUser(anyString(), anyString())).thenReturn(null);

        ResponseEntity<ApiResponse<?>> response = userController.loginUser(loginRequest, mockResponse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Login failed.", response.getBody().getMessage());
    }

    /**
     * Test: Successful user session authentication using a valid session ID
     */
    @Test
    void testAuthenticateUser_Success() {
        String userSession = EMPLOYEE_NUMBER.toString();
        when(userService.authenticateUser(anyLong())).thenReturn(mockEmployeeDTO);

        ResponseEntity<ApiResponse<?>> response = userController.authenticateUser(userSession);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Authentication successful.", response.getBody().getMessage());
    }

    /**
     * Test: Authentication with an empty session string
     */
    @Test
    void testAuthenticateUser_EmptySession() {
        String userSession = "";

        ResponseEntity<ApiResponse<?>> response = userController.authenticateUser(userSession);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Authentication failed.", response.getBody().getMessage());
    }

    /**
     * Test: Authentication fails due to invalid or expired session (returns null)
     */
    @Test
    void testAuthenticateUser_InvalidSession() {
        String userSession = EMPLOYEE_NUMBER.toString();
        when(userService.authenticateUser(anyLong())).thenReturn(null);

        ResponseEntity<ApiResponse<?>> response = userController.authenticateUser(userSession);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Authentication failed.", response.getBody().getMessage());
    }

    /**
     * Test: Successful logout by clearing session cookie
     */
    @Test
    void testLogoutUser_Success() {
        ResponseEntity<ApiResponse<?>> response = userController.logoutUser(mockResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Logout successful.", response.getBody().getMessage());
    }

    /**
     * Test: Logout fails due to an internal error when adding a cookie
     */
    @Test
    void testLogoutUser_Error() {
        HttpServletResponse errorResponse = mock(HttpServletResponse.class);
        doThrow(new RuntimeException("Error adding cookie"))
                .when(errorResponse).addCookie(any(jakarta.servlet.http.Cookie.class));

        ResponseEntity<ApiResponse<?>> response = userController.logoutUser(errorResponse);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Logout failed.", response.getBody().getMessage());
    }
}
