package com.oop.motorph.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.oop.motorph.entity.*;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EntityManager entityManager;

    // Test constants
    private static final Long EMPLOYEE_NUMBER_1 = 10026L;
    private static final Long EMPLOYEE_NUMBER_2 = 10027L;
    private static final Long NON_EXISTENT_EMPLOYEE_NUMBER = 99999L;
    private static final Long NEW_EMPLOYEE_NUMBER = 10028L;

    private Employee employee1;
    private Employee employee2;

    @BeforeEach
    void setUp() {
        // Create test employees
        employee1 = createTestEmployee(
                EMPLOYEE_NUMBER_1,
                "john.doe", "Passw0rd!",
                "Doe", "John", "1990-01-15", "123 Maple Street, Quezon City", "09171234567",
                "Regular", "Software Engineer", "IT Department",
                55000.0, 1200.0, 1100.0, 950.0, 27500.0, 343.75,
                "001234567890", "04-1234567-8", "987654321098", "123-456-789-000");

        employee2 = createTestEmployee(
                EMPLOYEE_NUMBER_2,
                "jane.smith", "SecureP@ss2!",
                "Smith", "Jane", "1992-03-20", "456 Pine Avenue, Makati City", "09187654321",
                "Probationary", "HR Manager", "Human Resources",
                62000.0, 1600.0, 1400.0, 1200.0, 31000.0, 387.50,
                "002345678901", "04-9876543-2", "876543210987", "987-654-321-001");

        employeeRepository.save(employee1);
        employeeRepository.save(employee2);
    }

    @AfterEach
    void resetAutoIncrement() {
        entityManager.createNativeQuery("ALTER TABLE employee ALTER COLUMN employeeNum RESTART WITH 10026")
                .executeUpdate();
    }

    // Helper method to create test employees
    private Employee createTestEmployee(Long employeeNumber, String username, String password,
            String lastName, String firstName, String birthdate,
            String address, String phoneNumber,
            String employmentStatus, String position, String department,
            Double basicSalary, Double riceSubsidy, Double phoneAllowance,
            Double clothingAllowance, Double grossSemiMonthlyRate, Double hourlyRate,
            String philhealth, String sss, String pagibig, String tin) {
        Employee employee = new Employee();
        employee.setEmployeeNumber(employeeNumber);
        employee.setUsername(username);
        employee.setPassword(password);
        employee.setPersonalInfo(new PersonalInfo(lastName, firstName, birthdate, address, phoneNumber));
        employee.setEmploymentInfo(new EmploymentInfo(employmentStatus, position, department));
        employee.setCompensation(new Compensation(null, basicSalary, riceSubsidy, phoneAllowance,
                clothingAllowance, grossSemiMonthlyRate, hourlyRate));
        employee.setGovernmentIds(new GovernmentIds(null, philhealth, sss, pagibig, tin));
        return employee;
    }

    @Test
    @Transactional
    @Rollback
    void testFindByEmployeeNumber() {
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER_1);

        assertTrue(foundEmployee.isPresent());
        assertEquals(employee1.getEmployeeNumber(), foundEmployee.get().getEmployeeNumber());
        assertEquals(employee1.getUsername(), foundEmployee.get().getUsername());
    }

    @Test
    @Transactional
    @Rollback
    void testFindByEmployeeNumber_NotFound() {
        Optional<Employee> foundEmployee = employeeRepository.findByEmployeeNumber(NON_EXISTENT_EMPLOYEE_NUMBER);
        assertFalse(foundEmployee.isPresent());
    }

    @Test
    @Transactional
    @Rollback
    void testFindAllEmployeeNumbers() {
        List<Long> employeeNumbers = employeeRepository.findAllEmployeeNumbers();

        assertNotNull(employeeNumbers);
        assertEquals(27, employeeNumbers.size());
        assertTrue(employeeNumbers.contains(EMPLOYEE_NUMBER_1));
        assertTrue(employeeNumbers.contains(EMPLOYEE_NUMBER_2));
    }

    @Test
    @Transactional
    @Rollback
    void testSaveEmployee() {
        Employee newEmployee = createTestEmployee(
                NEW_EMPLOYEE_NUMBER,
                "bob.brown", "NewP@ssw0rd3!",
                "Brown", "Bob", "1992-03-03", "789 Willow Lane, Pasig City", "09191122334",
                "Regular", "Data Analyst", "Data & Analytics",
                48000.0, 1100.0, 900.0, 800.0, 24000.0, 300.0,
                "3456789012", "04-5555555-5", "765432109876", "333-222-111-000");

        Employee savedEmployee = employeeRepository.save(newEmployee);

        assertNotNull(savedEmployee);
        assertEquals(NEW_EMPLOYEE_NUMBER, savedEmployee.getEmployeeNumber());
        assertEquals("bob.brown", savedEmployee.getUsername());
        assertNotNull(savedEmployee.getGovernmentIds());
        assertEquals("3456789012", savedEmployee.getGovernmentIds().getPhilhealth());
        assertEquals("04-5555555-5", savedEmployee.getGovernmentIds().getSss());
    }

    @Test
    @Transactional
    @Rollback
    void testUpdateEmployee() {
        // Update employee details
        employee1.setUsername("updated.john.doe");
        employee1.getPersonalInfo().setFirstName("Jonathan");
        employee1.getGovernmentIds().setPhilhealth("1234567899");
        employee1.getGovernmentIds().setTin("123-456-789-001");

        Employee updatedEmployee = employeeRepository.save(employee1);

        // Verify updates
        assertEquals("updated.john.doe", updatedEmployee.getUsername());
        assertEquals("Jonathan", updatedEmployee.getPersonalInfo().getFirstName());
        assertEquals("1234567899", updatedEmployee.getGovernmentIds().getPhilhealth());
        assertEquals("123-456-789-001", updatedEmployee.getGovernmentIds().getTin());
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteEmployee() {
        employeeRepository.deleteById(employee1.getUserId());

        Optional<Employee> deletedEmployee = employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER_1);
        assertFalse(deletedEmployee.isPresent());
    }
}