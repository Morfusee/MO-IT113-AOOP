package com.oop.motorph.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.User;
import com.oop.motorph.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeService employeeService;

    public EmployeeDTO loginUser(String username, String password) {
        // Find user by username and password
        User user = userRepository.findByUsernameAndPassword(username.trim(), password.trim());

        if (user.getPassword().trim().equals(password.trim()) && user.getUsername().trim().equals(username.trim())) {
            EmployeeDTO employee = employeeService.getEmployeeById(user.getUserId());

            // Return employee number if user is an employee
            return employee;
        }

        // Return null if user is not an employee
        return null;
    }

    public EmployeeDTO authenticateUser(Long employeeNum) {
        EmployeeDTO employee = employeeService.getEmployeeByEmployeeNum((employeeNum));
        
        if (employee != null) {
            // Return employee number if user is an employee
            return employee;
        }

        // Return null if user is not an employee
        return null;
    }
}
