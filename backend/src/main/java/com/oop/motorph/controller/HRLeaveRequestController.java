package com.oop.motorph.controller;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.entity.LeaveRequest;
import com.oop.motorph.service.LeaveRequestService;
import com.oop.motorph.utils.ApiResponse;

/**
 * REST controller for Human Resources (HR) to manage leave requests.
 * This controller provides endpoints for HR personnel to view, create, update,
 * and delete leave requests.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/leave-requests")
public class HRLeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    /**
     * Retrieves leave requests based on optional employee number and status.
     * This endpoint allows HR to fetch all leave requests, or filter by employee
     * number,
     * or by status, or a combination of employee number and status.
     *
     * @param employeeNum Optional employee number to filter leave requests.
     * @param status      Optional status (e.g., "Pending", "Approved", "Rejected")
     *                    to filter leave requests.
     *                    Defaults to "Pending" if not provided.
     * @return A ResponseEntity containing an ApiResponse with leave request data or
     *         an error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllLeaveRequests(@RequestParam(required = false) Long employeeNum,
            @RequestParam(required = false, defaultValue = "Pending") String status) {
        try {
            // If no employee number is provided, fetch all leave requests based on the
            // given status.
            if (employeeNum == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched all leave requests successfully.",
                        leaveRequestService.getLeaveRequestByStatus(status)));
            }

            // If an employee number is provided, fetch leave requests for that employee
            // with the given status.
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Fetched all leave requests successfully.",
                    leaveRequestService.getLeaveRequestByEmployeeNum(employeeNum, status)));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch leave requests.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Retrieves a single leave request by its unique ID.
     * This endpoint is useful for HR to view detailed information about a specific
     * leave request.
     *
     * @param id The unique ID of the leave request to retrieve.
     * @return A ResponseEntity containing an ApiResponse with the LeaveRequestDTO
     *         or an error message.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> getLeaveRequestById(@PathVariable Long id) {
        try {
            LeaveRequestDTO fetchedLeaveRequest = leaveRequestService.getLeaveRequestById(id);
            return ResponseEntity.ok()
                    .body(ApiResponse.success("Leave request fetched successfully.", fetchedLeaveRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch leave request.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Creates a new leave request.
     * This endpoint allows HR to submit a leave request on behalf of an employee or
     * for internal purposes.
     *
     * @param leaveRequest The LeaveRequest entity containing the data for the new
     *                     leave request.
     * @return A ResponseEntity containing an ApiResponse with the created
     *         LeaveRequestDTO or an error message.
     */
    @PostMapping("")
    public ResponseEntity<ApiResponse<?>> createLeaveRequest(@RequestBody LeaveRequest leaveRequest) {
        try {
            LeaveRequestDTO createdLeaveRequest = leaveRequestService.createLeaveRequest(leaveRequest);
            return ResponseEntity.ok()
                    .body(ApiResponse.created("Leave request created successfully.", createdLeaveRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to create leave request.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Updates an existing leave request.
     * This endpoint allows HR to modify details of a leave request, such as its
     * status (e.g., approve or reject).
     *
     * @param leaveRequest The LeaveRequestDTO containing the updated leave request
     *                     data.
     * @return A ResponseEntity containing an ApiResponse with the updated
     *         LeaveRequestDTO or an error message.
     */
    @PatchMapping("")
    public ResponseEntity<ApiResponse<?>> updateEmployee(
            @RequestBody LeaveRequestDTO leaveRequest) {
        try {
            LeaveRequestDTO updatedLeaveRequest = leaveRequestService.updateLeaveRequest(leaveRequest);
            return ResponseEntity.ok().body(
                    ApiResponse.success("Employee updated successfully.", updatedLeaveRequest));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to update employee.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Deletes a leave request by its unique ID.
     * This endpoint is used by HR to remove a leave request from the system.
     *
     * @param id The unique ID of the leave request to delete.
     * @return A ResponseEntity containing an ApiResponse with the deleted
     *         LeaveRequestDTO or an error message.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteLeaveRequestById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok().body(ApiResponse.success("Leave request deleted successfully.",
                    leaveRequestService.deleteLeaveRequestById(id)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to delete leave request.",
                    Map.of("error", e.getMessage())));
        }
    }
}