package com.oop.motorph.employee;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.mapper.EmployeeDTOMapper;
import com.oop.motorph.dto.mapper.EmployeeRequestDTOMapper;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.repository.EmployeeRepository;
import com.oop.motorph.utils.EmployeeTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class DeleteEmployeeTests {
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDTOMapper employeeDTOMapper;

    @Autowired
    private EmployeeRequestDTOMapper employeeRequestDTOMapper;

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    private Employee savedEmployee;

    private EmployeeDTO deletedEmployee;

    @BeforeEach
    public void setup() {
        savedEmployee = employeeTestUtils.createAndSaveEmployee();

        // Delete employee
        deletedEmployee = employeeTestUtils.deleteEmployee(savedEmployee.getEmployeeNumber());
    }

    @Test
    void testEmployeeNull() {
        assertNull(deletedEmployee);
    }
}
