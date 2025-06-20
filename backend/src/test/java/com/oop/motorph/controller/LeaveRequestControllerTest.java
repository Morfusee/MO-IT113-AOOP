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
public class LeaveRequestControllerTest {

    @InjectMocks
    private LeaveRequestController leaveRequestController;

    @Mock
    private LeaveRequestService leaveRequestService;

    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Long LEAVE_REQUEST_ID = 1L;
    private static final String STATUS = "Pending";

    private LeaveRequest mockLeaveRequest;
    private LeaveRequestDTO mockLeaveRequestDTO;
    private List<LeaveRequestDTO> mockLeaveRequestList;

    @BeforeEach
    void setup() {
        mockLeaveRequest = new LeaveRequest(
                EMPLOYEE_NUMBER,
                Timestamp.valueOf("2024-02-22 15:07:02"),
                Timestamp.valueOf("2024-02-24 15:07:03"),
                "Test notes",
                "Sick Leave",
                STATUS);

        mockLeaveRequestDTO = LeaveRequestDTO.builder()
                .id(1)
                .employeeNum(EMPLOYEE_NUMBER)
                .employeeName("John Doe")
                .startDate(Timestamp.valueOf("2024-02-22 15:07:02"))
                .endDate(Timestamp.valueOf("2024-02-24 15:07:03"))
                .notes("Test notes")
                .leaveType("Sick Leave")
                .status(STATUS)
                .build();

        mockLeaveRequestList = Arrays.asList(mockLeaveRequestDTO);
    }

    private void assertOkResponse(ResponseEntity<ApiResponse<?>> response, String expectedMessage) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody() != null, "Response body should not be null");
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    /**
     * Test: Successfully retrieves all leave requests for a specific employee and status.
     */
    @Test
    void testGetAllLeaveRequests_Success() {
        when(leaveRequestService.getLeaveRequestByEmployeeNum(anyLong(), anyString()))
                .thenReturn(mockLeaveRequestList);

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.getAllLeaveRequests(EMPLOYEE_NUMBER, STATUS);

        assertOkResponse(response, "Fetched all leave requests successfully.");
    }

    /**
     * Test: Handles error when fetching leave requests fails due to service exception.
     */
    @Test
    void testGetAllLeaveRequests_Error() {
        when(leaveRequestService.getLeaveRequestByEmployeeNum(anyLong(), anyString()))
                .thenThrow(new RuntimeException("Database error"));

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.getAllLeaveRequests(EMPLOYEE_NUMBER, STATUS);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully retrieves a leave request by its ID.
     */
    @Test
    void testGetLeaveRequestById_Success() {
        when(leaveRequestService.getLeaveRequestById(anyLong())).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.getLeaveRequestById(LEAVE_REQUEST_ID);

        assertOkResponse(response, "Leave request fetched successfully.");
    }

    /**
     * Test: Handles error when the leave request with given ID is not found.
     */
    @Test
    void testGetLeaveRequestById_Error() {
        when(leaveRequestService.getLeaveRequestById(anyLong()))
                .thenThrow(new RuntimeException("Leave request not found"));

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.getLeaveRequestById(LEAVE_REQUEST_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully creates a new leave request.
     */
    @Test
    void testCreateLeaveRequest_Success() {
        when(leaveRequestService.createLeaveRequest(any(LeaveRequest.class))).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.createLeaveRequest(mockLeaveRequest);

        assertOkResponse(response, "Leave request created successfully.");
    }

    /**
     * Test: Handles error when creating a leave request with invalid data.
     */
    @Test
    void testCreateLeaveRequest_Error() {
        when(leaveRequestService.createLeaveRequest(any(LeaveRequest.class)))
                .thenThrow(new RuntimeException("Invalid leave request data"));

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.createLeaveRequest(mockLeaveRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully deletes a leave request by ID.
     */
    @Test
    void testDeleteLeaveRequestById_Success() {
        when(leaveRequestService.deleteLeaveRequestById(anyLong())).thenReturn(mockLeaveRequestDTO);

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.deleteLeaveRequestById(LEAVE_REQUEST_ID);

        assertOkResponse(response, "Leave request deleted successfully.");
    }

    /**
     * Test: Handles error when trying to delete a non-existent leave request.
     */
    @Test
    void testDeleteLeaveRequestById_Error() {
        when(leaveRequestService.deleteLeaveRequestById(anyLong()))
                .thenThrow(new RuntimeException("Leave request not found"));

        ResponseEntity<ApiResponse<?>> response = leaveRequestController.deleteLeaveRequestById(LEAVE_REQUEST_ID);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
