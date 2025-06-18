package com.oop.motorph.repository;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.oop.motorph.entity.Compensation;
import jakarta.transaction.Transactional;

@DataJpaTest
class CompensationRepositoryTest {

    @Autowired
    private CompensationRepository compensationRepository;

    // Test constants
    private static final Double INITIAL_BASIC_SALARY = 50000.0;
    private static final Double INITIAL_RICE_SUBSIDY = 1000.0;
    private static final Double INITIAL_PHONE_ALLOWANCE = 1000.0;
    private static final Double INITIAL_CLOTHING_ALLOWANCE = 1000.0;
    private static final Double INITIAL_GROSS_SEMI_MONTHLY_RATE = 25000.0;
    private static final Double INITIAL_HOURLY_RATE = 312.50;
    
    private static final Double UPDATED_BASIC_SALARY = 55000.0;
    private static final Double UPDATED_RICE_SUBSIDY = 1500.0;
    private static final Double UPDATED_HOURLY_RATE = 343.75;

    private Compensation compensation;

    @BeforeEach
    void setUp() {
        compensation = new Compensation(
                null,
                INITIAL_BASIC_SALARY,
                INITIAL_RICE_SUBSIDY,
                INITIAL_PHONE_ALLOWANCE,
                INITIAL_CLOTHING_ALLOWANCE,
                INITIAL_GROSS_SEMI_MONTHLY_RATE,
                INITIAL_HOURLY_RATE);
    }

    // Helper method to assert compensation values
    private void assertCompensationValues(Compensation comp, Double basicSalary, Double riceSubsidy, 
                                        Double phoneAllowance, Double clothingAllowance,
                                        Double grossSemiMonthlyRate, Double hourlyRate) {
        assertEquals(basicSalary, comp.getBasicSalary());
        assertEquals(riceSubsidy, comp.getRiceSubsidy());
        assertEquals(phoneAllowance, comp.getPhoneAllowance());
        assertEquals(clothingAllowance, comp.getClothingAllowance());
        assertEquals(grossSemiMonthlyRate, comp.getGrossSemiMonthlyRate());
        assertEquals(hourlyRate, comp.getHourlyRate());
    }

    @Test
    @Transactional
    @Rollback
    void testSaveCompensation() {
        Compensation savedCompensation = compensationRepository.save(compensation);

        assertNotNull(savedCompensation);
        assertNotNull(savedCompensation.getCompensationId());
        assertCompensationValues(savedCompensation, 
                INITIAL_BASIC_SALARY, INITIAL_RICE_SUBSIDY, INITIAL_PHONE_ALLOWANCE,
                INITIAL_CLOTHING_ALLOWANCE, INITIAL_GROSS_SEMI_MONTHLY_RATE, INITIAL_HOURLY_RATE);
    }

    @Test
    @Transactional
    @Rollback
    void testFindCompensationById() {
        Compensation savedCompensation = compensationRepository.save(compensation);
        Compensation foundCompensation = compensationRepository.findById(savedCompensation.getCompensationId())
                .orElse(null);

        assertNotNull(foundCompensation);
        assertEquals(savedCompensation.getCompensationId(), foundCompensation.getCompensationId());
        assertCompensationValues(foundCompensation,
                INITIAL_BASIC_SALARY, INITIAL_RICE_SUBSIDY, INITIAL_PHONE_ALLOWANCE,
                INITIAL_CLOTHING_ALLOWANCE, INITIAL_GROSS_SEMI_MONTHLY_RATE, INITIAL_HOURLY_RATE);
    }

    @Test
    @Transactional
    @Rollback
    void testUpdateCompensation() {
        Compensation savedCompensation = compensationRepository.save(compensation);

        // Update fields
        savedCompensation.setBasicSalary(UPDATED_BASIC_SALARY);
        savedCompensation.setRiceSubsidy(UPDATED_RICE_SUBSIDY);
        savedCompensation.setHourlyRate(UPDATED_HOURLY_RATE);

        Compensation updatedCompensation = compensationRepository.save(savedCompensation);

        // Verify updated fields and unchanged fields
        assertCompensationValues(updatedCompensation,
                UPDATED_BASIC_SALARY, UPDATED_RICE_SUBSIDY, INITIAL_PHONE_ALLOWANCE,
                INITIAL_CLOTHING_ALLOWANCE, INITIAL_GROSS_SEMI_MONTHLY_RATE, UPDATED_HOURLY_RATE);
    }

    @Test
    @Transactional
    @Rollback
    void testDeleteCompensation() {
        Compensation savedCompensation = compensationRepository.save(compensation);
        Long compensationId = savedCompensation.getCompensationId();

        compensationRepository.deleteById(compensationId);

        assertTrue(compensationRepository.findById(compensationId).isEmpty());
    }

    @Test
    @Transactional
    @Rollback
    void testCompensationDefaultConstructor() {
        Compensation defaultCompensation = new Compensation();

        assertNotNull(defaultCompensation);
        assertNull(defaultCompensation.getCompensationId());
        assertNull(defaultCompensation.getBasicSalary());
        assertNull(defaultCompensation.getRiceSubsidy());
        assertNull(defaultCompensation.getPhoneAllowance());
        assertNull(defaultCompensation.getClothingAllowance());
        assertNull(defaultCompensation.getGrossSemiMonthlyRate());
        assertNull(defaultCompensation.getHourlyRate());
    }
}