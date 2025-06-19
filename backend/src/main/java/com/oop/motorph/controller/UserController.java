package com.oop.motorph.controller;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.service.UserService;
import com.oop.motorph.utils.ApiResponse;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

/**
 * REST controller for user authentication and session management.
 * Handles user login, authentication via session cookie, and logout.
 */
@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * Handles user login by verifying credentials and setting a session cookie upon
     * successful login.
     *
     * @param body     A map containing "username" and "password" from the request
     *                 body.
     * @param response The HttpServletResponse to add the session cookie to.
     * @return A ResponseEntity with an ApiResponse indicating login success or
     *         failure.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> loginUser(
            @RequestBody(required = true) Map<String, String> body,
            HttpServletResponse response) {
        try {
            if (!body.containsKey("username") || !body.containsKey("password")) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Login failed.",
                        Map.of("error", "Username and password are required.")));
            }

            EmployeeDTO employee = userService.loginUser(body.get("username"), body.get("password"));

            if (employee.equals(null)) { // Consider using Objects.isNull(employee) for null checks.
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Login failed.",
                        Map.of("error", "Invalid username or password.")));
            }

            // Create a cookie if login is successful
            Cookie authCookie = new Cookie("userSession", employee.employee().getEmployeeNumber().toString());
            authCookie.setMaxAge(24 * 60 * 60); // Set cookie expiry to 24 hours
            authCookie.setHttpOnly(true); // Prevent client-side script access to the cookie
            authCookie.setSecure(true); // Only send cookie over HTTPS
            authCookie.setPath("/"); // Make cookie available across the entire application

            response.addCookie(authCookie); // Attach cookie to response

            return ResponseEntity.ok().body(ApiResponse.success(
                    "Login successful.",
                    employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Login failed.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Authenticates a user based on the provided session cookie.
     *
     * @param userSession The value of the "userSession" cookie from the request.
     * @return A ResponseEntity with an ApiResponse indicating authentication
     *         success or failure.
     */
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<?>> authenticateUser(
            @CookieValue(value = "userSession") String userSession) {
        try {
            if (userSession.isEmpty()) { // Use isEmpty() for string checks. Objects.isNull() for objects.
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Authentication failed.", // Changed message to reflect authentication rather than login
                        Map.of("error", "Session cookie is missing or empty."))); // More specific error message
            }

            EmployeeDTO employee = userService.authenticateUser(Long.parseLong(userSession));

            if (employee.equals(null)) { // Consider using Objects.isNull(employee) for null checks.
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Authentication failed.", // Changed message to reflect authentication rather than login
                        Map.of("error", "Invalid session or user not found."))); // More specific error message
            }

            return ResponseEntity.ok().body(ApiResponse.success(
                    "Authentication successful.", // Changed message
                    employee));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Authentication failed.", // Changed message
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Handles user logout by invalidating the session cookie.
     *
     * @param response The HttpServletResponse to invalidate the session cookie.
     * @return A ResponseEntity with an ApiResponse indicating logout success or
     *         failure.
     */
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutUser(HttpServletResponse response) {
        try {
            // Create a cookie with max age 0 to delete it
            Cookie authCookie = new Cookie("userSession", "");
            authCookie.setMaxAge(0); // Set cookie expiry to immediately expire
            authCookie.setHttpOnly(true); // Prevent client-side script access
            authCookie.setSecure(true); // Only send cookie over HTTPS
            authCookie.setPath("/"); // Ensure it covers the entire application

            response.addCookie(authCookie); // Attach cookie to response

            return ResponseEntity.ok().body(ApiResponse.success(
                    "Logout successful.",
                    Map.of("message", "User logged out.")));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Logout failed.",
                    Map.of("error", e.getMessage())));
        }
    }

}