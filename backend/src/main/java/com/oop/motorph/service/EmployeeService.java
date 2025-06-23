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

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GovernmentIdRepository governmentIdRepository;

    @Autowired
    private CompensationRepository compensationRepository;

    @Autowired
    private EmployeeDTOMapper employeeDTOMapper;

    @Autowired
    private EmployeeRequestDTOMapper employeeRequestDTOMapper;

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves all employees.
     * 
     * @return List of EmployeeDTOs.
     */
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeDTOMapper)
                .toList();
    }

    /**
     * Retrieves an employee by database ID.
     * 
     * @param employeeNum The database ID of the employee.
     * @return EmployeeDTO if found, otherwise null.
     */
    public EmployeeDTO getEmployeeById(Long employeeNum) {
        return employeeRepository.findById(employeeNum).stream()
                .map(employeeDTOMapper)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves an employee by employee number.
     * 
     * @param employeeNum The unique employee number.
     * @return EmployeeDTO if found, otherwise null.
     */
    public EmployeeDTO getEmployeeByEmployeeNum(Long employeeNum) {
        return employeeRepository.findByEmployeeNumber(employeeNum).stream()
                .map(employeeDTOMapper)
                .findFirst()
                .orElse(null);
    }

    /**
     * Retrieves all employee numbers.
     * 
     * @return List of employee numbers.
     */
    public List<Long> getAllEmployeeNums() {
        return employeeRepository.findAllEmployeeNumbers();
    }

}
