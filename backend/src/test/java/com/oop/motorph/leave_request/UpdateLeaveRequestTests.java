package com.oop.motorph.leave_request;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.utils.LeaveRequestTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class UpdateLeaveRequestTests {
    @Autowired
    private LeaveRequestTestUtils leaveRequestTestUtils;

    private LeaveRequestDTO savedLeaveRequest;

    private LeaveRequestDTO updatedLeaveRequest;

    @BeforeEach
    public void setup() {
        // Create and save leave request
        savedLeaveRequest = leaveRequestTestUtils.createAndSaveLeaveRequest();

        // Update leave request
        updatedLeaveRequest = leaveRequestTestUtils.updateLeaveRequest(savedLeaveRequest);
    }

    @Test
    void testIdStillSame() {
        assertEquals(savedLeaveRequest.id(), updatedLeaveRequest.id());
    }

    @Test
    void testEmployeeNumStillSame() {
        assertEquals(savedLeaveRequest.employeeNum(), updatedLeaveRequest.employeeNum());
    }

    @Test
    void testStartDateStillSame() {
        assertEquals(savedLeaveRequest.startDate(), updatedLeaveRequest.startDate());
    }

    @Test
    void testEndDateStillSame() {
        assertEquals(savedLeaveRequest.endDate(), updatedLeaveRequest.endDate());
    }

    @Test
    void testLeaveTypeStillSame() {
        assertEquals(savedLeaveRequest.leaveType(), updatedLeaveRequest.leaveType());
    }

    @Test
    void testNotesStillSame() {
        assertEquals(savedLeaveRequest.notes(), updatedLeaveRequest.notes());
    }

    @Test
    void testStatusDiff() {
        assertEquals("Denied", updatedLeaveRequest.status());
    }

}
