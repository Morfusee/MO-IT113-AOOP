package com.oop.motorph.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.oop.motorph.entity.*;
import jakarta.transaction.Transactional;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    // Test constants
    private static final Long EMPLOYEE_NUMBER = 10026L;
    private static final String USERNAME = "john.doe";
    private static final String PASSWORD = "Passw0rd!";
    private static final String UPDATED_USERNAME = "updateduser";
    private static final String UPDATED_PASSWORD = "updatedpass";
    private static final String INVALID_CREDENTIAL = "wrong";

    private User savedUser;

    @BeforeEach
    @Transactional
    @Rollback
    public void setup() {
        Employee user = createTestUser(
            EMPLOYEE_NUMBER,
            USERNAME,
            PASSWORD,
            "Doe", "John", "1990-01-15", 
            "123 Maple Street, Quezon City", "09171234567",
            "Regular", "Software Engineer", "IT Department",
            55000.0, 1200.0, 1100.0, 950.0, 27500.0, 343.75,
            "001234567890", "04-1234567-8", "987654321098", "123-456-789-000"
        );

        savedUser = userRepository.save(user);
    }

    // Helper method to create test user
    private Employee createTestUser(Long employeeNumber, String username, String password,
                                  String lastName, String firstName, String birthdate,
                                  String address, String phoneNumber,
                                  String employmentStatus, String position, String department,
                                  Double basicSalary, Double riceSubsidy, Double phoneAllowance,
                                  Double clothingAllowance, Double grossSemiMonthlyRate, Double hourlyRate,
                                  String philhealth, String sss, String pagibig, String tin) {
        Employee user = new Employee();
        user.setEmployeeNumber(employeeNumber);
        user.setUsername(username);
        user.setPassword(password);
        user.setPersonalInfo(new PersonalInfo(lastName, firstName, birthdate, address, phoneNumber));
        user.setEmploymentInfo(new EmploymentInfo(employmentStatus, position, department));
        user.setCompensation(new Compensation(null, basicSalary, riceSubsidy, phoneAllowance, 
                                           clothingAllowance, grossSemiMonthlyRate, hourlyRate));
        user.setGovernmentIds(new GovernmentIds(null, philhealth, sss, pagibig, tin));
        return user;
    }

    @Test
    @Transactional
    @Rollback
    public void testSaveUser() {
        assertNotNull(savedUser);
        assertNotNull(savedUser.getUserId());
        assertEquals(USERNAME, savedUser.getUsername());
        assertEquals(PASSWORD, savedUser.getPassword());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByUsernameAndPassword() {
        User found = userRepository.findByUsernameAndPassword(USERNAME, PASSWORD);
        
        assertNotNull(found);
        assertEquals(savedUser.getUserId(), found.getUserId());
        assertEquals(USERNAME, found.getUsername());
        assertEquals(PASSWORD, found.getPassword());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByUsernameAndPasswordWithInvalidCredentials() {
        User notFound = userRepository.findByUsernameAndPassword(INVALID_CREDENTIAL, INVALID_CREDENTIAL);
        assertNull(notFound);
    }

    @Test
    @Transactional
    @Rollback
    public void testExistsByUsername() {
        assertTrue(userRepository.existsByUsername(USERNAME));
        assertFalse(userRepository.existsByUsername(INVALID_CREDENTIAL));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteUser() {
        userRepository.delete(savedUser);
        assertFalse(userRepository.existsById(savedUser.getUserId()));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateUser() {
        savedUser.setUsername(UPDATED_USERNAME);
        savedUser.setPassword(UPDATED_PASSWORD);

        User updatedUser = userRepository.save(savedUser);

        assertNotNull(updatedUser);
        assertEquals(UPDATED_USERNAME, updatedUser.getUsername());
        assertEquals(UPDATED_PASSWORD, updatedUser.getPassword());
    }
}