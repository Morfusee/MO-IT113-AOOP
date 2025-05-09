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

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Verify if login credentials are correct
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

            if (employee.equals(null)) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Login failed.",
                        Map.of("error", "Invalid username or password.")));
            }

            // Create a cookie if login is successful
            Cookie authCookie = new Cookie("userSession", employee.employee().getEmployeeNumber().toString());
            authCookie.setMaxAge(24 * 60 * 60);
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            authCookie.setPath("/");

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

    // Authenticate user
    @PostMapping("/authenticate")
    public ResponseEntity<ApiResponse<?>> authenticateUser(
            @CookieValue(value = "userSession") String userSession) {
        try {
            if (userSession.equals("") || userSession.equals(null)) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Login failed.",
                        Map.of("error", "Username and password are required.")));
            }

            EmployeeDTO employee = userService.authenticateUser(Long.parseLong(userSession));

            if (employee.equals(null)) {
                return ResponseEntity.badRequest().body(ApiResponse.error(
                        Response.SC_BAD_REQUEST,
                        "Login failed.",
                        Map.of("error", "Invalid username or password.")));
            }

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

    // Logout user
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logoutUser(HttpServletResponse response) {
        try {
            Cookie authCookie = new Cookie("userSession", "");
            authCookie.setMaxAge(0);
            authCookie.setHttpOnly(true);
            authCookie.setSecure(true);
            authCookie.setPath("/");

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
