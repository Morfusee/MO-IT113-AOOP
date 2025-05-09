package com.oop.motorph.leave_request;

import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.utils.LeaveRequestTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class DeleteLeaveRequestTests {

    @Autowired
    private LeaveRequestTestUtils leaveRequestTestUtils;

    private LeaveRequestDTO savedLeaveRequest;

    private LeaveRequestDTO deletedLeaveRequest;

    @BeforeEach
    public void setup() {
        // Create and save leave request
        savedLeaveRequest = leaveRequestTestUtils.createAndSaveLeaveRequest();

        // Delete leave request
        deletedLeaveRequest = leaveRequestTestUtils.deleteLeaveRequest(
                savedLeaveRequest);
    }

    @Test
    public void testLeaveRequestNull() {
        assertNull(deletedLeaveRequest);
    }
}
