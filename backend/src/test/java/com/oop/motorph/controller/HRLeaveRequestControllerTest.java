package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.entity.LeaveRequest;
import com.oop.motorph.service.LeaveRequestService;
import com.oop.motorph.utils.ApiResponse;

@ExtendWith(MockitoExtension.class)
public class HRLeaveRequestControllerTest {

    @InjectMocks
    private HRLeaveRequestController hrLeaveRequestController;

    @Mock
    private LeaveRequestService leaveRequestService;

    private LeaveRequest mockLeaveRequest;
    private LeaveRequestDTO mockLeaveRequestDTO;
    private List<LeaveRequestDTO> mockLeaveRequestList;

    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Long LEAVE_REQUEST_ID = 1L;
    private static final String STATUS = "Pending";

    @BeforeEach
    void setup() {
        // Initialize mock data
        mockLeaveRequest = new LeaveRequest(
                EMPLOYEE_NUMBER,
                Timestamp.valueOf("2024-02-22 15:07:02"),
                Timestamp.valueOf("2024-02-24 15:07:03"),
                "Test notes",
                "Sick Leave",
                "Pending");

        mockLeaveRequestDTO = LeaveRequestDTO.builder()
                .id(1)
                .employeeNum(EMPLOYEE_NUMBER)
                .employeeName("John Doe")
                .startDate(Timestamp.valueOf("2024-02-22 15:07:02"))
                .endDate(Timestamp.valueOf("2024-02-24 15:07:03"))
                .notes("Test notes")
                .leaveType("Sick Leave")
                .status("Pending")
                .build();

        mockLeaveRequestList = Arrays.asList(mockLeaveRequestDTO);
    }

    private void assertOkResponse(ResponseEntity<ApiResponse<?>> response, String expectedMessage) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody() != null, "Response body should not be null");
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    /**
     * Test: Successfully retrieve all leave requests by status.
     */
    @Test
    void testGetAllLeaveRequests_Success() {
        when(leaveRequestService.getLeaveRequestByStatus(anyString())).thenReturn(mockLeaveRequestList);

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.getAllLeaveRequests(null, STATUS);

        assertOkResponse(response, "Fetched all leave requests successfully.");
    }

    /**
     * Test: Error handling when retrieving leave requests by status.
     */
    @Test
    void testGetAllLeaveRequests_Error() {
        when(leaveRequestService.getLeaveRequestByStatus(anyString()))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.getAllLeaveRequests(null, STATUS);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully retrieve leave requests by employee number and status.
     */
    @Test
    void testGetLeaveRequestByEmployeeNum_Success() {
        when(leaveRequestService.getLeaveRequestByEmployeeNum(anyLong(), anyString()))
                .thenReturn(mockLeaveRequestList);

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.getAllLeaveRequests(EMPLOYEE_NUMBER, STATUS);

        assertOkResponse(response, "Fetched all leave requests successfully.");
    }

    /**
     * Test: Error handling when retrieving leave requests by employee number.
     */
    @Test
    void testGetLeaveRequestByEmployeeNum_Error() {
        when(leaveRequestService.getLeaveRequestByEmployeeNum(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Employee not found"));

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.getAllLeaveRequests(EMPLOYEE_NUMBER, STATUS);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully fetch a leave request by its ID.
     */
    @Test
    void testGetLeaveRequestById_Success() {
        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.getLeaveRequestById(LEAVE_REQUEST_ID);

        assertOkResponse(response, "Leave request fetched successfully.");
    }

    /**
     * Test: Error handling when fetching a non-existent leave request.
     */
    @Test
    void testGetLeaveRequestById_Error() {
        when(leaveRequestService.getLeaveRequestById(anyLong()))
                .thenThrow(new RuntimeException("Leave request not found"));

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.getLeaveRequestById(LEAVE_REQUEST_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully create a new leave request.
     */
    @Test
    void testCreateLeaveRequest_Success() {
        when(leaveRequestService.createLeaveRequest(any(LeaveRequest.class))).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.createLeaveRequest(mockLeaveRequest);

        assertOkResponse(response, "Leave request created successfully.");
    }

    /**
     * Test: Error handling when creating a leave request with invalid data.
     */
    @Test
    void testCreateLeaveRequest_Error() {
        when(leaveRequestService.createLeaveRequest(any(LeaveRequest.class)))
                .thenThrow(new RuntimeException("Invalid leave request data"));

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.createLeaveRequest(mockLeaveRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully update a leave request.
     */
    @Test
    void testUpdateLeaveRequest_Success() {
        when(leaveRequestService.updateLeaveRequest(any(LeaveRequestDTO.class))).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.updateEmployee(mockLeaveRequestDTO);

        assertOkResponse(response, "Employee updated successfully.");
    }

    /**
     * Test: Error handling when updating a non-existent leave request.
     */
    @Test
    void testUpdateLeaveRequest_Error() {
        when(leaveRequestService.updateLeaveRequest(any(LeaveRequestDTO.class)))
                .thenThrow(new RuntimeException("Leave request not found"));

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.updateEmployee(mockLeaveRequestDTO);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully delete a leave request by ID.
     */
    @Test
    void testDeleteLeaveRequest_Success() {
        when(leaveRequestService.deleteLeaveRequestById(anyLong())).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.deleteLeaveRequestById(LEAVE_REQUEST_ID);

        assertOkResponse(response, "Leave request deleted successfully.");
    }

    /**
     * Test: Error handling when deleting a non-existent leave request.
     */
    @Test
    void testDeleteLeaveRequest_Error() {
        when(leaveRequestService.deleteLeaveRequestById(anyLong()))
                .thenThrow(new RuntimeException("Leave request not found"));

        ResponseEntity<ApiResponse<?>> response = hrLeaveRequestController.deleteLeaveRequestById(LEAVE_REQUEST_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
