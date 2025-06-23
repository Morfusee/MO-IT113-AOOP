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
 * REST controller for Human Resources (HR) to manage attendance records and
 * analytics.
 * This controller provides endpoints for HR personnel to view attendance data
 * for all employees or specific employees,
 * with optional date filtering.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/attendances")
public class HRAttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceAnalyticsService attendanceAnalyticsService;

    /**
     * Retrieves attendance records based on optional employee number and date
     * range.
     * This endpoint allows HR to fetch all attendance records, or filter by
     * employee number,
     * or by a date range, or a combination of employee number and date range.
     *
     * @param employeeNum  Optional employee number to filter attendance records.
     * @param startDateStr Optional start date string (yyyy-MM-dd) to filter
     *                     attendance records.
     * @param endDateStr   Optional end date string (yyyy-MM-dd) to filter
     *                     attendance records.
     * @return A ResponseEntity containing an ApiResponse with attendance data or an
     *         error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getAttendances(
            @RequestParam(required = false) Long employeeNum,
            @RequestParam(required = false, value = "startDate") String startDateStr,
            @RequestParam(required = false, value = "endDate") String endDateStr) {

        try {
            // Manually parse the String to LocalDate, then convert to java.sql.Date
            Date startDate = (startDateStr != null) ? Date.valueOf(LocalDate.parse(startDateStr)) : null;
            Date endDate = (endDateStr != null) ? Date.valueOf(LocalDate.parse(endDateStr)) : null;

            // If no parameters are provided, return all attendances in the database
            if (employeeNum == null && startDate == null && endDate == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched all attendances successfully.",
                        attendanceService.getAllAttendances()));
            }

            // If only employeeNum is provided, return all attendances of the employee
            if (employeeNum != null && startDate == null && endDate == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched employee attendances successfully.",
                        attendanceService.getAttendanceByEmployeeNum(employeeNum)));
            }

            // If only endDate is provided, set startDate to the first day of the month
            if (startDate == null && endDate != null) {
                startDate = Date.valueOf(endDate.toLocalDate().withDayOfMonth(1));
            }

            // If only startDate is provided, set endDate to the last day of the month
            if (endDate == null && startDate != null) {
                endDate = Date.valueOf(startDate.toLocalDate().withDayOfMonth(startDate.toLocalDate().lengthOfMonth()));
            }
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Fetched employee attendances successfully.",
                    attendanceService.getAttendanceByEmployeeNum(employeeNum, startDate, endDate)));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch attendances.",
                    Map.of("error", e.getMessage())));
        }

    }

    /**
     * Retrieves a single attendance record by its unique ID.
     * This endpoint is useful for HR to view detailed information about a specific
     * attendance entry.
     *
     * @param attendanceId The unique ID of the attendance record to retrieve.
     * @return A ResponseEntity containing an ApiResponse with the AttendanceDTO or
     *         an error message.
     */
    @GetMapping("/{attendanceId}")
    public ResponseEntity<ApiResponse<?>> getAttendanceById(@PathVariable Long attendanceId) {
        try {
            AttendanceDTO fetchedAttendance = attendanceService.getAttendanceById(attendanceId);
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Attendance fetched successfully.",
                    fetchedAttendance));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch attendance.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Retrieves attendance analytics based on optional employee number and date
     * range.
     * This endpoint provides aggregated attendance data for HR, allowing them to
     * see
     * overall trends or specific employee analytics.
     *
     * @param employeeNum  Optional employee number to filter attendance analytics.
     * @param startDateStr Optional start date string (yyyy-MM-dd) for the analytics
     *                     period.
     * @param endDateStr   Optional end date string (yyyy-MM-dd) for the analytics
     *                     period.
     * @return A ResponseEntity containing an ApiResponse with attendance analytics
     *         data or an error message.
     */
    @GetMapping("/analytics")
    public ResponseEntity<ApiResponse<?>> getAttendancesAnalytics(
            @RequestParam(required = false) Long employeeNum,
            @RequestParam(required = false, value = "startDate") String startDateStr,
            @RequestParam(required = false, value = "endDate") String endDateStr) {

        try {
            // Manually parse the String to LocalDate, then convert to java.sql.Date
            Date startDate = (startDateStr != null) ? Date.valueOf(LocalDate.parse(startDateStr)) : null;
            Date endDate = (endDateStr != null) ? Date.valueOf(LocalDate.parse(endDateStr)) : null;

            // If no parameters are provided, return all attendances in the database
            if (employeeNum == null && startDate == null && endDate == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched all attendance analytics successfully.",
                        attendanceAnalyticsService.getAllAttendanceAnalytics()));
            }

            // If only employeeNum is provided, return all attendances of the employee
            if (employeeNum != null && startDate == null && endDate == null) {
                return ResponseEntity.ok().body(ApiResponse.success(
                        "Fetched all attendance analytics successfully.",
                        attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(employeeNum)));
            }

            // If only endDate is provided, set startDate to the first day of the month
            if (startDate == null && endDate != null) {
                startDate = Date.valueOf(endDate.toLocalDate().withDayOfMonth(1));
            }

            // If only startDate is provided, set endDate to the last day of the month
            if (endDate == null && startDate != null) {
                endDate = Date.valueOf(startDate.toLocalDate().withDayOfMonth(startDate.toLocalDate().lengthOfMonth()));
            }

            // Return all attendances of the employee within the specified date range
            return ResponseEntity.ok().body(ApiResponse.success(
                    "Fetched all attendance analytics successfully.",
                    attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(employeeNum, startDate,
                            endDate)));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(ApiResponse.error(
                    Response.SC_BAD_REQUEST,
                    "Failed to fetch attendance analytics.",
                    Map.of("error", e.getMessage())));
        }
    }
}