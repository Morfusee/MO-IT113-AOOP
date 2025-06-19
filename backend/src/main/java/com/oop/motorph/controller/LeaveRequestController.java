package com.oop.motorph.controller;

import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
 * REST controller for managing employee leave requests.
 * This controller provides endpoints for employees to view, create, and delete
 * their own leave requests.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/leave-requests")
public class LeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    /**
     * Retrieves leave requests for a specific employee, optionally filtered by
     * status.
     * This endpoint is intended for employees to view their own leave requests.
     *
     * @param employeeNum The employee number for whom to retrieve leave requests
     *                    (required).
     * @param status      Optional status (e.g., "Pending", "Approved", "Rejected")
     *                    to filter leave requests.
     *                    Defaults to "Pending" if not provided.
     * @return A ResponseEntity containing an ApiResponse with a list of
     *         LeaveRequestDTOs or an error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllLeaveRequests(@RequestParam(required = true) Long employeeNum,
            @RequestParam(required = false, defaultValue = "Pending") String status) {
        try {
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
     * This endpoint allows an employee to view detailed information about a
     * specific leave request they submitted.
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
     * This endpoint allows an employee to submit a new leave request.
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
     * Deletes a leave request by its unique ID.
     * This endpoint allows an employee to withdraw a submitted leave request.
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