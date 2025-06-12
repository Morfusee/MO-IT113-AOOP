package com.oop.motorph.controller;


import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList; // Example for data
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import javax.sql.DataSource; // If you are using a database connection for your reports


import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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


    @GetMapping("/payroll")
    public ResponseEntity<?> generateEmployeePayrollReport(
            @RequestParam(required = false) String title,
            @RequestParam String userId, // Required for this report
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, // Required
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) { // Required


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
            // Use a Global Exception Handler for a cleaner approach
            // For now, keeping your current response logic for illustration
            return ResponseEntity.badRequest().body(ApiResponse.badRequestException(e.getMessage()));
        } catch (Exception e) {
            // General catch for other exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    "Error generating payroll report.",
                    Map.of("error", e.getMessage())));
        }
    }


    @GetMapping("/payroll/annual")
    public ResponseEntity<?> generateEmployeeAnnualPayrollReport(
            @RequestParam(required = false) String title,
            @RequestParam String userId, // Required for this report
            @RequestParam Integer year) { // Required


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


    @GetMapping("/payroll/annual/summary")
    public ResponseEntity<?> generateAllEmployeesAnnualPayrollReport(
            @RequestParam(required = false) String title,
            @RequestParam Integer year) { // Required


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



