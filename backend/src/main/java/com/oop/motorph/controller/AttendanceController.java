package com.oop.motorph.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.service.AttendanceAnalyticsService;
import com.oop.motorph.service.AttendanceService;
import com.oop.motorph.utils.ApiResponse;

/**
 * REST controller for managing attendance-related operations.
 * Handles API requests for fetching attendance records and analytics.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/attendances")
public class AttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceAnalyticsService attendanceAnalyticsService;

    /**
     * Retrieves attendance records based on employee number and an optional date
     * range.
     *
     * @param employeeNum  The employee number for which to retrieve attendance.
     * @param startDateStr Optional start date string (yyyy-MM-dd) for the
     *                     attendance period.
     * @param endDateStr   Optional end date string (yyyy-MM-dd) for the attendance
     *                     period.
     * @return A ResponseEntity containing an ApiResponse with attendance data or an
     *         error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAttendances(
            @RequestParam(required = true) Long employeeNum,
            @RequestParam(required = false, value = "startDate") String startDateStr,
            @RequestParam(required = false, value = "endDate") String endDateStr) {

        try {
            // Manually parse the String to LocalDate, then convert to java.sql.Date
            Date startDate = (startDateStr != null) ? Date.valueOf(LocalDate.parse(startDateStr)) : null;
            Date endDate = (endDateStr != null) ? Date.valueOf(LocalDate.parse(endDateStr)) : null;

            // If only employeeNum is provided, return all attendances of the employee
            if (employeeNum != null && startDate == null && endDate == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched employee attendances successfully.",
                        attendanceService.getAttendanceByEmployeeNum(employeeNum)));
            }

            // Adjust start and end dates if only one is provided to cover the full month
            if (startDate == null && endDate != null) {
                startDate = Date.valueOf(endDate.toLocalDate().withDayOfMonth(1));
            }

            if (endDate == null && startDate != null) {
                endDate = Date.valueOf(startDate.toLocalDate().withDayOfMonth(startDate.toLocalDate().lengthOfMonth()));
            }

            // Return all attendances of the employee within the specified date range
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Fetched employee attendances successfully.",
                    attendanceService.getAttendanceByEmployeeNum(employeeNum, startDate, endDate)));

        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST, // HTTP status code for bad request.
                    "Failed to fetch attendances.",
                    Map.of("error", e.getMessage()))); // Include error message in the response.
        }
    }

    /**
     * Retrieves a single attendance record by its ID.
     *
     * @param attendanceId The ID of the attendance record to retrieve.
     * @return A ResponseEntity containing an ApiResponse with the attendance data
     *         or an error message.
     */
    @GetMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<?>> getAttendanceById(@PathVariable Long attendanceId) {
        try {
            AttendanceDTO fetchedAttendance = attendanceService.getAttendanceById(attendanceId);
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Attendance fetched successfully.",
                    fetchedAttendance));
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST, // HTTP status code for bad request.
                    "Failed to fetch attendance.",
                    Map.of("error", e.getMessage()))); // Include error message in the response.
        }
    }

    /**
     * Retrieves attendance analytics based on employee number and an optional date
     * range.
     *
     * @param employeeNum  The employee number for which to retrieve attendance
     *                     analytics.
     * @param startDateStr Optional start date string (yyyy-MM-dd) for the analytics
     *                     period.
     * @param endDateStr   Optional end date string (yyyy-MM-dd) for the analytics
     *                     period.
     * @return A ResponseEntity containing an ApiResponse with attendance analytics
     *         data or an error message.
     */
    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<?>> getAttendancesAnalytics(
            @RequestParam(required = true) Long employeeNum,
            @RequestParam(required = false, value = "startDate") String startDateStr,
            @RequestParam(required = false, value = "endDate") String endDateStr) {

        try {
            // Manually parse the String to LocalDate, then convert to java.sql.Date
            Date startDate = (startDateStr != null) ? Date.valueOf(LocalDate.parse(startDateStr)) : null;
            Date endDate = (endDateStr != null) ? Date.valueOf(LocalDate.parse(endDateStr)) : null;

            // If only employeeNum is provided, return all attendance analytics of the
            // employee
            if (employeeNum != null && startDate == null && endDate == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched all attendance analytics successfully.",
                        attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(employeeNum)));
            }

            // Adjust start and end dates if only one is provided to cover the full month
            if (startDate == null && endDate != null) {
                startDate = Date.valueOf(endDate.toLocalDate().withDayOfMonth(1));
            }

            if (endDate == null && startDate != null) {
                endDate = Date.valueOf(startDate.toLocalDate().withDayOfMonth(startDate.toLocalDate().lengthOfMonth()));
            }

            // Return all attendance analytics of the employee within the specified date
            // range
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Fetched all attendance analytics successfully.",
                    attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(employeeNum, startDate,
                            endDate)));
        } catch (Exception e) {
            // Handle exceptions and return an error response
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST, // HTTP status code for bad request.
                    "Failed to fetch attendance analytics.",
                    Map.of("error", e.getMessage()))); // Include error message in the response.
        }
    }
}