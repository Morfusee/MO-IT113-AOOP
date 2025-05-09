package com.oop.motorph.employee;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.entity.Employee;
import com.oop.motorph.utils.EmployeeTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class CreateAndReadEmployeeTests {
    @Autowired
    private EmployeeTestUtils employeeTestUtils;

    private Employee savedEmployee;

    @BeforeEach
    public void setup() {
        savedEmployee = employeeTestUtils.createAndSaveEmployee();
    }

    @Test
    void testEmployeeNotNull() {
        assertNotNull(savedEmployee);
    }

    @Test
    void testUserIdNotNull() {
        assertNotNull(savedEmployee.getUserId());
    }

    @Test
    void testEmployeeNumberNotNull() {
        assertNotNull(savedEmployee.getEmployeeNumber());
    }

    @Test
    void testUsername() {
        assertEquals(savedEmployee.getUsername(), "Morfuse");
    }

    @Test
    void testFirstName() {
        assertEquals(savedEmployee.getPersonalInfo().getFirstName(), "Markel");
    }

    @Test
    void testLastName() {
        assertEquals(savedEmployee.getPersonalInfo().getLastName(), "Vale");
    }
}
