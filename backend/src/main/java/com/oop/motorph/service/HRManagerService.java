package com.oop.motorph.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.dto.mapper.EmployeeDTOMapper;
import com.oop.motorph.dto.mapper.EmployeeRequestDTOMapper;
import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.GovernmentIds;
import com.oop.motorph.repository.CompensationRepository;
import com.oop.motorph.repository.EmployeeRepository;
import com.oop.motorph.repository.GovernmentIdRepository;
import com.oop.motorph.repository.UserRepository;
import com.oop.motorph.utils.ApiResponse;

@Service
public class HRManagerService {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmployeeDTOMapper employeeDTOMapper;

    @Autowired
    private EmployeeRequestDTOMapper employeeRequestDTOMapper;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private GovernmentIdRepository governmentIdRepository;

    // Get all employees
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(employeeDTOMapper).toList();
    }

    // Save operation
    public EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequest) {

        // Map EmployeeRequestDTO to Employee
        Employee employee = employeeRequestDTOMapper.toEmployee(employeeRequest);

        // Check first if the username exists already
        if (userRepository.existsByUsername(employee.getUsername())) {
            throw new RuntimeException(ApiResponse.badRequestException("Username is already taken").getMessage());
        }

        // Save and explicitly set GovernmentIds
        if (employeeRequest.governmentIds() != null) {
            GovernmentIds governmentIds = governmentIdRepository.save(employee.getGovernmentIds());
            employee.setGovernmentIds(governmentIds);
        }

        // Save and explicitly set Compensation
        if (employeeRequest.compensation() != null) {
            Compensation compensation = compensationRepository.save(employee.getCompensation());
            employee.setCompensation(compensation);
        }

        // Save employee (with governmentIds & compensation)
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    // Update operation
    public EmployeeDTO updateEmployee(Long employeeNum, EmployeeRequestDTO employeeRequest) {

        // Check first if the username exists already
        if (userRepository.existsByUsername(employeeRequest.user().username())) {
            throw new RuntimeException(ApiResponse.badRequestException("Username is already taken").getMessage());
        }

        // Get employee by employee number
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNum).orElseThrow(
                () -> new RuntimeException(ApiResponse.entityNotFoundException("Employee not found").getMessage()));

        // Update employee
        employeeRequestDTOMapper.updateEntity(employeeRequest, employee);

        // Save and explicitly set GovernmentIds
        if (employeeRequest.governmentIds() != null) {
            GovernmentIds governmentIds = governmentIdRepository.save(employee.getGovernmentIds());
            employee.setGovernmentIds(governmentIds);
        }

        // Save and explicitly set Compensation
        if (employeeRequest.compensation() != null) {
            Compensation compensation = compensationRepository.save(employee.getCompensation());
            employee.setCompensation(compensation);
        }

        // Save employee (with updated governmentIds & compensation)
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    // Delete operation
    public EmployeeDTO deleteEmployeeById(Long employeeNum) {
        // Find employee by employee number
        EmployeeDTO employeeDTO = employeeRepository.findByEmployeeNumber(employeeNum).map(employeeDTOMapper)
                .orElseThrow(
                        () -> new RuntimeException(
                                ApiResponse.entityNotFoundException("Employee not found").getMessage()));

        // Delete employee using user id
        employeeRepository.deleteById(employeeDTO.employee().getUserId());

        return employeeDTO;
    }

}
