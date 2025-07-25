package com.oop.motorph.repository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.oop.motorph.entity.GovernmentIds;

import jakarta.transaction.Transactional;

@DataJpaTest
public class GovernmentIdRepositoryTest {

    @Autowired
    private GovernmentIdRepository governmentIdRepository;

    // Test constants for initial values
    private static final String INITIAL_PHILHEALTH = "123456789";
    private static final String INITIAL_SSS = "SSS-123456789";
    private static final String INITIAL_PAGIBIG = "123456789";
    private static final String INITIAL_TIN = "TIN-123456789";

    // Test constants for updated values
    private static final String UPDATED_PHILHEALTH = "987654321";
    private static final String UPDATED_SSS = "SSS-987654321";
    private static final String UPDATED_PAGIBIG = "987654321";
    private static final String UPDATED_TIN = "TIN-987654321";

    private GovernmentIds savedGovernmentIds;

    /**
     * Initializes a GovernmentIds entity before each test.
     */
    @BeforeEach
    @Transactional
    @Rollback
    public void setup() {
        GovernmentIds governmentIds = new GovernmentIds(
                INITIAL_PHILHEALTH, INITIAL_SSS, INITIAL_PAGIBIG, INITIAL_TIN);
        savedGovernmentIds = governmentIdRepository.save(governmentIds);
    }

    /**
     * Helper method to verify values in a {@link GovernmentIds} entity.
     */
    private void assertGovernmentIds(GovernmentIds ids,
            String philhealth, String sss,
            String pagibig, String tin) {
        assertEquals(philhealth, ids.getPhilhealth());
        assertEquals(sss, ids.getSss());
        assertEquals(pagibig, ids.getPagibig());
        assertEquals(tin, ids.getTin());
    }

    /**
     * Tests creation and saving of GovernmentIds entity.
     */
    @Test
    @Transactional
    @Rollback
    public void testCreateGovernmentIds() {
        assertNotNull(savedGovernmentIds);
        assertNotNull(savedGovernmentIds.getGovernmentId());
        assertGovernmentIds(savedGovernmentIds,
                INITIAL_PHILHEALTH, INITIAL_SSS, INITIAL_PAGIBIG, INITIAL_TIN);
    }

    /**
     * Tests updating fields in an existing GovernmentIds entity.
     */
    @Test
    @Transactional
    @Rollback
    public void testUpdateGovernmentIds() {
        savedGovernmentIds.setPhilhealth(UPDATED_PHILHEALTH);
        savedGovernmentIds.setSss(UPDATED_SSS);
        savedGovernmentIds.setPagibig(UPDATED_PAGIBIG);
        savedGovernmentIds.setTin(UPDATED_TIN);

        GovernmentIds updatedGovernmentIds = governmentIdRepository.save(savedGovernmentIds);

        assertNotNull(updatedGovernmentIds);
        assertGovernmentIds(updatedGovernmentIds,
                UPDATED_PHILHEALTH, UPDATED_SSS, UPDATED_PAGIBIG, UPDATED_TIN);
    }

    /**
     * Tests deletion of a GovernmentIds entity.
     */
    @Test
    @Transactional
    @Rollback
    public void testDeleteGovernmentIds() {
        Long governmentId = savedGovernmentIds.getGovernmentId();
        governmentIdRepository.deleteById(governmentId);
        assertFalse(governmentIdRepository.existsById(governmentId));
    }

    /**
     * Tests retrieval of a GovernmentIds entity by ID.
     */
    @Test
    @Transactional
    @Rollback
    public void testFindGovernmentIdsById() {
        GovernmentIds foundGovernmentIds = governmentIdRepository
                .findById(savedGovernmentIds.getGovernmentId())
                .orElse(null);

        assertNotNull(foundGovernmentIds);
        assertGovernmentIds(foundGovernmentIds,
                INITIAL_PHILHEALTH, INITIAL_SSS, INITIAL_PAGIBIG, INITIAL_TIN);
    }
}
