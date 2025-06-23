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

    /**
     * Retrieves all employees.
     * 
     * @return List of EmployeeDTOs.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream().map(employeeDTOMapper).toList();
    }

    /**
     * Creates a new employee.
     * 
     * @param employeeRequest The request payload containing employee data.
     * @return The created EmployeeDTO.
     */
    public EmployeeDTO createEmployee(EmployeeRequestDTO employeeRequest) {

        // Map EmployeeRequestDTO to Employee entity
        Employee employee = employeeRequestDTOMapper.toEmployee(employeeRequest);

        // Check if the username is already taken
        if (userRepository.existsByUsername(employee.getUsername())) {
            throw new RuntimeException(ApiResponse.badRequestException("Username is already taken").getMessage());
        }

        // Save government IDs if provided
        if (employeeRequest.governmentIds() != null) {
            GovernmentIds governmentIds = governmentIdRepository.save(employee.getGovernmentIds());
            employee.setGovernmentIds(governmentIds);
        }

        // Save compensation if provided
        if (employeeRequest.compensation() != null) {
            Compensation compensation = compensationRepository.save(employee.getCompensation());
            employee.setCompensation(compensation);
        }

        // Save the complete employee record
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    /**
     * Updates an existing employee.
     * 
     * @param employeeNum     The employee number to update.
     * @param employeeRequest The new employee data.
     * @return The updated EmployeeDTO.
     */
    public EmployeeDTO updateEmployee(Long employeeNum, EmployeeRequestDTO employeeRequest) {

        // Check if the username is already taken
        if (userRepository.existsByUsername(employeeRequest.user().username())) {
            throw new RuntimeException(ApiResponse.badRequestException("Username is already taken").getMessage());
        }

        // Retrieve the existing employee
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNum).orElseThrow(
                () -> new RuntimeException(ApiResponse.entityNotFoundException("Employee not found").getMessage()));

        // Update fields from request
        employeeRequestDTOMapper.updateEntity(employeeRequest, employee);

        // Update government IDs if provided
        if (employeeRequest.governmentIds() != null) {
            GovernmentIds governmentIds = governmentIdRepository.save(employee.getGovernmentIds());
            employee.setGovernmentIds(governmentIds);
        }

        // Update compensation if provided
        if (employeeRequest.compensation() != null) {
            Compensation compensation = compensationRepository.save(employee.getCompensation());
            employee.setCompensation(compensation);
        }

        // Save updated employee
        return employeeDTOMapper.apply(employeeRepository.save(employee));
    }

    /**
     * Deletes an employee by their employee number.
     * 
     * @param employeeNum The employee number of the employee to delete.
     * @return The deleted EmployeeDTO.
     */
    public EmployeeDTO deleteEmployeeById(Long employeeNum) {
        // Retrieve employee
        EmployeeDTO employeeDTO = employeeRepository.findByEmployeeNumber(employeeNum)
                .map(employeeDTOMapper)
                .orElseThrow(() -> new RuntimeException(
                        ApiResponse.entityNotFoundException("Employee not found").getMessage()));

        // Delete employee using user ID
        employeeRepository.deleteById(employeeDTO.employee().getUserId());

        return employeeDTO;
    }

}
