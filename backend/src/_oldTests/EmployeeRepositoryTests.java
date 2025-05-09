package com.oop.motorph;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.mapper.EmployeeDTOMapper;
import com.oop.motorph.dto.mapper.EmployeeRequestDTOMapper;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.repository.EmployeeRepository;
import com.oop.motorph.utils.EmployeeTestUtils;

import jakarta.transaction.Transactional;

@DataJpaTest
@Disabled
public class EmployeeRepositoryTests {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDTOMapper employeeDTOMapper;

    @Autowired
    private EmployeeRequestDTOMapper employeeRequestDTOMapper; // Create instance manually

    private Employee savedEmployee;

    @BeforeEach
    @Transactional
    @Rollback
    public void setup() {
        savedEmployee = EmployeeTestUtils.createAndSaveEmployee(employeeRepository, employeeRequestDTOMapper);
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateEmployee() {
        assertNotNull(savedEmployee);
        assertNotNull(savedEmployee.getUserId());
        assertNotNull(savedEmployee.getEmployeeNumber());
        assertEquals(savedEmployee.getUsername(), "Morfuse");
        assertEquals(savedEmployee.getPersonalInfo().getFirstName(), "Markel");
        assertEquals(savedEmployee.getPersonalInfo().getLastName(), "Vale");
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateEmployee() {
        String newFirstName = "Fultz";
        String newLastName = "Vale";
        String newStatus = "Probationary";
        Double newRiceSubsidy = 2000D;
        String newSSS = "123333";
        String newUsername = "MarkelFultz";

        savedEmployee.getPersonalInfo().setFirstName(newFirstName);
        savedEmployee.getPersonalInfo().setLastName(newLastName);
        savedEmployee.getEmploymentInfo().setStatus(newStatus);
        savedEmployee.getCompensation().setRiceSubsidy(newRiceSubsidy);
        savedEmployee.getGovernmentIds().setSss(newSSS);
        savedEmployee.setUsername(newUsername);

        Employee updatedEmployee = employeeRepository.save(savedEmployee);

        assertNotNull(updatedEmployee);
        assertNotNull(updatedEmployee.getUserId());
        assertNotNull(updatedEmployee.getEmployeeNumber());
        assertEquals(updatedEmployee.getPersonalInfo().getFirstName(), newFirstName);
        assertEquals(updatedEmployee.getPersonalInfo().getLastName(), newLastName);
        assertEquals(updatedEmployee.getEmploymentInfo().getStatus(), newStatus);
        assertEquals(updatedEmployee.getCompensation().getRiceSubsidy(), newRiceSubsidy);
        assertEquals(updatedEmployee.getGovernmentIds().getSss(), newSSS);
        assertEquals(updatedEmployee.getUsername(), newUsername);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteEmployee() {
        Long deletedEmployeeNum = savedEmployee.getEmployeeNumber();

        // Find employee by employee number
        EmployeeDTO employeeDTO = employeeRepository.findByEmployeeNumber(deletedEmployeeNum)
                .map(employeeDTOMapper).orElse(null);

        // Delete employee using user id
        employeeRepository.deleteById(employeeDTO.employee().getUserId());

        // Try checking if the user still exists in the database
        EmployeeDTO deletedEmployeeDTO = employeeRepository.findByEmployeeNumber(deletedEmployeeNum)
                .map(employeeDTOMapper).orElse(null);

        assertNull(deletedEmployeeDTO);
    }
}
