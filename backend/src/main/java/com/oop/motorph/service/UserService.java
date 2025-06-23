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

    /**
     * Validates user login credentials and returns associated employee details.
     *
     * @param username The username entered.
     * @param password The password entered.
     * @return EmployeeDTO if valid employee user, otherwise null.
     */
    public EmployeeDTO loginUser(String username, String password) {
        User user = userRepository.findByUsernameAndPassword(username.trim(), password.trim());

        if (user != null &&
                user.getPassword().trim().equals(password.trim()) &&
                user.getUsername().trim().equals(username.trim())) {
            return employeeService.getEmployeeById(user.getUserId());
        }

        return null;
    }

    /**
     * Authenticates a user based on employee number.
     *
     * @param employeeNum The employee number.
     * @return EmployeeDTO if valid employee, otherwise null.
     */
    public EmployeeDTO authenticateUser(Long employeeNum) {
        EmployeeDTO employee = employeeService.getEmployeeByEmployeeNum(employeeNum);

        return (employee != null) ? employee : null;
    }
}
