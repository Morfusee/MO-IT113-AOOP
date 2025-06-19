package com.oop.motorph.controller;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.service.EmployeeService;
import com.oop.motorph.service.PayrollService;
import com.oop.motorph.service.ReportService;
import com.oop.motorph.utils.ApiResponse;

import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.JRException;

/**
 * REST controller for generating various reports, primarily focusing on
 * payroll.
 * This controller provides endpoints to generate individual employee payroll
 * reports,
 * annual payroll reports for a single employee, and a summary of annual payroll
 * reports for all employees.
 */
@RestController
@RequestMapping("/reports")
@CrossOrigin(origins = "http://localhost:5173/")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @Autowired
    private PayrollService payrollService;

    @Autowired
    private EmployeeService employeeService;

    /**
     * Generates a payroll report for a specific employee within a given date range.
     * The report is returned as a PDF file.
     *
     * @param title     Optional title for the report.
     * @param userId    The employee ID (user ID) for whom the payroll report is
     *                  generated.
     * @param startDate The start date of the payroll period.
     * @param endDate   The end date of the payroll period.
     * @return A ResponseEntity containing the PDF report bytes, or an error
     *         response.
     */
    @GetMapping("/payroll")
    public ResponseEntity<?> generateEmployeePayrollReport(
            @RequestParam(required = false) String title,
            @RequestParam String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        try {
            byte[] reportBytes = reportService.generateEmployeePayrollReport(userId, startDate, endDate, title);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "payroll-report.pdf");
            headers.setContentLength(reportBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);

        } catch (IllegalArgumentException | EntityNotFoundException e) {
            // Handle specific business logic exceptions for bad requests
            return ResponseEntity.badRequest().body(ApiResponse.badRequestException(e.getMessage()));
        } catch (Exception e) {
            // General catch for other unexpected exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error generating payroll report.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Generates an annual payroll report for a specific employee for a given year.
     * The report is returned as a PDF file.
     *
     * @param title  Optional title for the report.
     * @param userId The employee ID (user ID) for whom the annual payroll report is
     *               generated.
     * @param year   The year for which the annual payroll report is generated.
     * @return A ResponseEntity containing the PDF report bytes, or an error
     *         response.
     */
    @GetMapping("/payroll/annual")
    public ResponseEntity<?> generateEmployeeAnnualPayrollReport(
            @RequestParam(required = false) String title,
            @RequestParam String userId,
            @RequestParam Integer year) {

        try {
            byte[] reportBytes = reportService.generateEmployeeAnnualPayrollReport(userId, year, title);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "annual-payroll-report-" + year + ".pdf");
            headers.setContentLength(reportBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);

        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequestException(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error generating annual payroll report.",
                    Map.of("error", e.getMessage())));
        }
    }

    /**
     * Generates a summary annual payroll report for all employees for a given year.
     * This report provides an overview of annual payroll data across the entire
     * organization.
     * The report is returned as a PDF file.
     *
     * @param title Optional title for the report.
     * @param year  The year for which the annual payroll summary report is
     *              generated.
     * @return A ResponseEntity containing the PDF report bytes, or an error
     *         response.
     */
    @GetMapping("/payroll/annual/summary")
    public ResponseEntity<?> generateAllEmployeesAnnualPayrollReport(
            @RequestParam(required = false) String title,
            @RequestParam Integer year) {

        try {
            byte[] reportBytes = reportService.generateAllEmployeesAnnualPayrollReport(year, title);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDispositionFormData("filename", "annual-payroll-report-summary" + year + ".pdf");
            headers.setContentLength(reportBytes.length);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(reportBytes);

        } catch (IllegalArgumentException | EntityNotFoundException e) {
            return ResponseEntity.badRequest().body(ApiResponse.badRequestException(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error generating annual payroll report.",
                    Map.of("error", e.getMessage())));
        }
    }

}