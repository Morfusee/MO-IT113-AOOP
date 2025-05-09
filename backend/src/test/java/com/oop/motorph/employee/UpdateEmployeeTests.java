package com.oop.motorph.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.entity.Employee;
import com.oop.motorph.utils.EmployeeTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class UpdateEmployeeTests {

    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    private Employee updatedEmployee;

    private Employee savedEmployee;

    @BeforeEach
    public void setup() {
        savedEmployee = employeeTestUtils.createAndSaveEmployee();

        updatedEmployee = employeeTestUtils.updateEmployee(savedEmployee);
    }

    @Test
    void testFirstName() {
        assertEquals(updatedEmployee.getPersonalInfo().getFirstName(), employeeTestUtils.newFirstName);
    }

    @Test
    void testLastName() {
        assertEquals(updatedEmployee.getPersonalInfo().getLastName(), employeeTestUtils.newLastName);
    }

    @Test
    void testStatus() {
        assertEquals(updatedEmployee.getEmploymentInfo().getStatus(), employeeTestUtils.newStatus);
    }

    @Test
    void testRiceSubsidy() {
        assertEquals(updatedEmployee.getCompensation().getRiceSubsidy(), employeeTestUtils.newRiceSubsidy);
    }

    @Test
    void testSSS() {
        assertEquals(updatedEmployee.getGovernmentIds().getSss(), employeeTestUtils.newSSS);
    }

    @Test
    void testUsername() {
        assertEquals(updatedEmployee.getUsername(), employeeTestUtils.newUsername);
    }
}