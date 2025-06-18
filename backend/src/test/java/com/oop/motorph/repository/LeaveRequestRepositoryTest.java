package com.oop.motorph.repository;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Timestamp;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import com.oop.motorph.entity.LeaveRequest;
import jakarta.transaction.Transactional;

@DataJpaTest
public class LeaveRequestRepositoryTest {

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;

    // Test constants
    private static final Long EMPLOYEE_NUMBER = 10022L;
    private static final String NOTES = "Test leave request";
    private static final String LEAVE_TYPE = "Sick Leave";
    private static final String STATUS = "Pending";
    private static final Timestamp REQUEST_DATE = Timestamp.valueOf("2024-01-27 17:45:37");
    private static final Timestamp LEAVE_DATE = Timestamp.valueOf("2024-02-27 17:45:37");

    private LeaveRequest savedLeaveRequest;

    @BeforeEach
    @Transactional
    @Rollback
    public void setup() {
        leaveRequestRepository.deleteAll();
        LeaveRequest leaveRequest = new LeaveRequest(
                EMPLOYEE_NUMBER,
                REQUEST_DATE,
                LEAVE_DATE,
                NOTES,
                LEAVE_TYPE,
                STATUS);
        savedLeaveRequest = leaveRequestRepository.save(leaveRequest);
    }

    // Helper method to assert leave request properties
    private void assertLeaveRequestProperties(LeaveRequest request) {
        assertEquals(EMPLOYEE_NUMBER, request.getEmployeeNum());
        assertEquals(NOTES, request.getNotes());
        assertEquals(LEAVE_TYPE, request.getLeaveType());
        assertEquals(STATUS, request.getStatus());
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByEmployeeNum() {
        List<LeaveRequest> found = leaveRequestRepository.findByEmployeeNum(EMPLOYEE_NUMBER);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(EMPLOYEE_NUMBER, found.get(0).getEmployeeNum());
        assertLeaveRequestProperties(found.get(0));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindById() {
        LeaveRequest found = leaveRequestRepository.findById(savedLeaveRequest.getId().intValue())
                .orElse(null);

        assertNotNull(found);
        assertEquals(savedLeaveRequest.getId(), found.getId());
        assertLeaveRequestProperties(found);
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByStatus() {
        List<LeaveRequest> found = leaveRequestRepository.findByStatus(STATUS);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(STATUS, found.get(0).getStatus());
        assertLeaveRequestProperties(found.get(0));
    }

    @Test
    @Transactional
    @Rollback
    public void testFindByEmployeeNumAndStatus() {
        List<LeaveRequest> found = leaveRequestRepository.findByEmployeeNumAndStatus(EMPLOYEE_NUMBER, STATUS);

        assertNotNull(found);
        assertFalse(found.isEmpty());
        assertEquals(EMPLOYEE_NUMBER, found.get(0).getEmployeeNum());
        assertEquals(STATUS, found.get(0).getStatus());
        assertLeaveRequestProperties(found.get(0));
    }

    @Test
    @Transactional
    @Rollback
    public void testSave() {
        assertNotNull(savedLeaveRequest);
        assertNotNull(savedLeaveRequest.getId());
        assertLeaveRequestProperties(savedLeaveRequest);
    }

    @Test
    @Transactional
    @Rollback
    public void testDelete() {
        leaveRequestRepository.delete(savedLeaveRequest);
        List<LeaveRequest> found = leaveRequestRepository.findByEmployeeNum(EMPLOYEE_NUMBER);
        assertTrue(found.isEmpty());
    }
}