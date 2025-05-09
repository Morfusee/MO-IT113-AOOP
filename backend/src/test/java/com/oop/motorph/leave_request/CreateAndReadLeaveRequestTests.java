package com.oop.motorph.leave_request;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.sql.Timestamp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.utils.LeaveRequestTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class CreateAndReadLeaveRequestTests {

    @Autowired
    private LeaveRequestTestUtils leaveRequestTestUtils;

    private LeaveRequestDTO savedLeaveRequest;

    @BeforeEach
    public void setup() {
        savedLeaveRequest = leaveRequestTestUtils.createAndSaveLeaveRequest();
    }

    @Test
    public void testLeaveRequestNotNull() {
        assertNotNull(savedLeaveRequest);
    }

    @Test
    public void testEmployeeNum() {
        assertEquals(savedLeaveRequest.employeeNum(), 10001L);
    }

    @Test
    public void testStartDate() {
        assertEquals(savedLeaveRequest.startDate(), Timestamp.valueOf("2024-01-27 17:45:37"));
    }

    @Test
    public void testEndDate() {
        assertEquals(savedLeaveRequest.endDate(), Timestamp.valueOf("2024-02-27 17:45:37"));
    }

    @Test
    public void testNotes() {
        assertEquals(savedLeaveRequest.notes(), "");
    }

    @Test
    public void testLeaveType() {
        assertEquals(savedLeaveRequest.leaveType(), "Sick Leave");
    }

    @Test
    public void testStatus() {
        assertEquals(savedLeaveRequest.status(), "Pending");
    }
}

// * "employeeNum": 10001,
// * "startDate": "2024-02-22T15:07:02.000+00:00",
// * "endDate": "2024-02-24T15:07:03.000+00:00",
// * "notes": "adasdsadsadsda",
// * "leaveType": "Sick Leave",
// * "status": "Approved"
// * }