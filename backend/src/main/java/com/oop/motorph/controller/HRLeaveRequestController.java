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

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.dto.LeaveRequestDTO;
import com.oop.motorph.entity.LeaveRequest;
import com.oop.motorph.service.LeaveRequestService;
import com.oop.motorph.utils.ApiResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/leave-requests")
public class HRLeaveRequestController {

    @Autowired
    private LeaveRequestService leaveRequestService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAllLeaveRequests(@RequestParam(required = false) Long employeeNum,
            @RequestParam(required = false, defaultValue = "Pending") String status) {
        try {
            // If fetching all leave requests (ALL)
            if (employeeNum == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        " Fetched all leave requests successfully.",
                        leaveRequestService.getLeaveRequestByStatus(status)));
            }

            // If fetching created by HR Manager or User (CREATED BY ME)
            return ResponseEntity.ok().body(ApiResponse.success(
                    " Fetched all leave requests successfully.",
                    leaveRequestService.getLeaveRequestByEmployeeNum(employeeNum, status)));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch leave requests.",
                    Map.of("error", e.getMessage())));
        }
    }

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

// Example post request
/*
 * {
 * "employeeNum": 10001,
 * "startDate": "2024-02-22T15:07:02.000+00:00",
 * "endDate": "2024-02-24T15:07:03.000+00:00",
 * "notes": "adasdsadsadsda",
 * "leaveType": "Sick Leave",
 * "status": "Approved"
 * }
 */
