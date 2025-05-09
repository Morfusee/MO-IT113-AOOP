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

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/attendances")
public class HRAttendanceController {

    @Autowired
    private AttendanceService attendanceService;

    @Autowired
    private AttendanceAnalyticsService attendanceAnalyticsService;

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

            // Get the first day of the month
            if (startDate == null && endDate != null) {
                startDate = Date.valueOf(endDate.toLocalDate().withDayOfMonth(1));
            }

            // Get the last day of the month
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

            // Get the first day of the month
            if (startDate == null && endDate != null) {
                startDate = Date.valueOf(endDate.toLocalDate().withDayOfMonth(1));
            }

            // Get the last day of the month
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

// Example post request
/*
 * {
 * "employeeNumber": 10002,
 * "date": "2025-02-17",
 * "timeIn": "08:00:00",
 * "timeOut": "17:00:00"
 * }
 */