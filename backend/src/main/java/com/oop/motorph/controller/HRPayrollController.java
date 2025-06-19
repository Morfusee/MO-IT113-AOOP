package com.oop.motorph.controller;

import java.sql.Date;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oop.motorph.service.PayrollService;
import com.oop.motorph.utils.ApiResponse;

/**
 * REST controller for Human Resources (HR) to manage payroll operations.
 * This controller provides endpoints for HR personnel to retrieve payroll
 * information and available payroll periods.
 */
@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/manager/payroll")
public class HRPayrollController {

    @Autowired
    private PayrollService payrollService;

    /**
     * Retrieves the payroll for a specific employee within a given date range.
     * This endpoint is used by HR to generate or view an employee's payroll details
     * for a defined period.
     *
     * @param employeeNum  The employee number for whom to retrieve the payroll.
     * @param startDateStr The start date of the payroll period in "yyyy-MM-dd"
     *                     format.
     * @param endDateStr   The end date of the payroll period in "yyyy-MM-dd"
     *                     format.
     * @return A ResponseEntity containing an ApiResponse with the payroll data or
     *         an error message.
     */
    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getPayroll(@RequestParam(required = true) Long employeeNum,
            @RequestParam(required = true, name = "startDate") String startDateStr,
            @RequestParam(required = true, name = "endDate") String endDateStr) {

        try {
            // Convert String date parameters to java.sql.Date objects.
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            return ResponseEntity.ok().body(ApiResponse.success("Fetched payroll successfully.",
                    payrollService.generatePayrollForPeriod(employeeNum, startDate, endDate)));
        } catch (Exception e) {
            // Catch any exceptions during the process and return a bad request error
            // response.
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(Response.SC_BAD_REQUEST, "Failed to fetch payroll.",
                            Map.of("error", e.getMessage())));
        }
    }

    /**
     * Retrieves a list of available payroll start dates for a given year.
     * This endpoint helps HR identify valid payroll periods for which data can be
     * fetched.
     *
     * @param year The year for which to retrieve payroll start dates.
     * @return A ResponseEntity containing an ApiResponse with a list of payroll
     *         start dates or an error message.
     */
    @GetMapping("/months")
    public ResponseEntity<ApiResponse<?>> getPayrollDates(@RequestParam(required = true) Integer year) {
        try {
            return ResponseEntity.ok().body(ApiResponse.success("Fetched payroll months successfully.",
                    payrollService.getPayrollStartDatesForYear(year)));
        } catch (Exception e) {
            // Catch any exceptions and return a bad request error response.
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(Response.SC_BAD_REQUEST, "Failed to fetch payroll months.",
                            Map.of("error", e.getMessage())));
        }
    }

}