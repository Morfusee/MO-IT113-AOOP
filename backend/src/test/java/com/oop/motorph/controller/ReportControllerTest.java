package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.oop.motorph.service.EmployeeService;
import com.oop.motorph.service.PayrollService;
import com.oop.motorph.service.ReportService;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class ReportControllerTest {

    @InjectMocks
    private ReportController reportController;

    @Mock
    private ReportService reportService;

    @Mock
    private PayrollService payrollService;

    @Mock
    private EmployeeService employeeService;

    private static final String EMPLOYEE_NUMBER = "10001";
    private static final Integer YEAR = 2024;
    private static final String TITLE = "Test Report";
    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 31);
    private static final byte[] REPORT_BYTES = "Test Report Content".getBytes();

    /**
     * Test: Successfully generate a payroll report PDF for a single employee within
     * a date range
     */
    @Test
    void testGenerateEmployeePayrollReport_Success() throws Exception {
        when(reportService.generateEmployeePayrollReport(anyString(), any(LocalDate.class), any(LocalDate.class),
                anyString()))
                .thenReturn(REPORT_BYTES);

        ResponseEntity<?> response = reportController.generateEmployeePayrollReport(TITLE, EMPLOYEE_NUMBER, START_DATE,
                END_DATE);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(REPORT_BYTES, response.getBody());
    }

    /** Test: Return 400 BAD_REQUEST if the employee is not found */
    @Test
    void testGenerateEmployeePayrollReport_EntityNotFound() throws Exception {
        when(reportService.generateEmployeePayrollReport(anyString(), any(LocalDate.class), any(LocalDate.class),
                anyString()))
                .thenThrow(new EntityNotFoundException("Employee not found"));

        ResponseEntity<?> response = reportController.generateEmployeePayrollReport(TITLE, EMPLOYEE_NUMBER, START_DATE,
                END_DATE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /** Test: Return 500 INTERNAL_SERVER_ERROR for unexpected exceptions */
    @Test
    void testGenerateEmployeePayrollReport_InternalError() throws Exception {
        when(reportService.generateEmployeePayrollReport(anyString(), any(LocalDate.class), any(LocalDate.class),
                anyString()))
                .thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<?> response = reportController.generateEmployeePayrollReport(TITLE, EMPLOYEE_NUMBER, START_DATE,
                END_DATE);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /**
     * Test: Successfully generate an annual payroll report for a single employee
     */
    @Test
    void testGenerateEmployeeAnnualPayrollReport_Success() throws Exception {
        when(reportService.generateEmployeeAnnualPayrollReport(anyString(), anyInt(), anyString()))
                .thenReturn(REPORT_BYTES);

        ResponseEntity<?> response = reportController.generateEmployeeAnnualPayrollReport(TITLE, EMPLOYEE_NUMBER, YEAR);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(REPORT_BYTES, response.getBody());
    }

    /** Test: Handle case when employee is not found for annual report */
    @Test
    void testGenerateEmployeeAnnualPayrollReport_EntityNotFound() throws Exception {
        when(reportService.generateEmployeeAnnualPayrollReport(anyString(), anyInt(), anyString()))
                .thenThrow(new EntityNotFoundException("Employee not found"));

        ResponseEntity<?> response = reportController.generateEmployeeAnnualPayrollReport(TITLE, EMPLOYEE_NUMBER, YEAR);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Return 500 if error occurs while generating individual annual report
     */
    @Test
    void testGenerateEmployeeAnnualPayrollReport_InternalError() throws Exception {
        when(reportService.generateEmployeeAnnualPayrollReport(anyString(), anyInt(), anyString()))
                .thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<?> response = reportController.generateEmployeeAnnualPayrollReport(TITLE, EMPLOYEE_NUMBER, YEAR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

    /** Test: Successfully generate an annual payroll report for all employees */
    @Test
    void testGenerateAllEmployeesAnnualPayrollReport_Success() throws Exception {
        when(reportService.generateAllEmployeesAnnualPayrollReport(anyInt(), anyString()))
                .thenReturn(REPORT_BYTES);

        ResponseEntity<?> response = reportController.generateAllEmployeesAnnualPayrollReport(TITLE, YEAR);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(MediaType.APPLICATION_PDF, response.getHeaders().getContentType());
        assertEquals(REPORT_BYTES, response.getBody());
    }

    /**
     * Test: Return 400 if no employees found when generating report for all
     * employees
     */
    @Test
    void testGenerateAllEmployeesAnnualPayrollReport_EntityNotFound() throws Exception {
        when(reportService.generateAllEmployeesAnnualPayrollReport(anyInt(), anyString()))
                .thenThrow(new EntityNotFoundException("No employees found"));

        ResponseEntity<?> response = reportController.generateAllEmployeesAnnualPayrollReport(TITLE, YEAR);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Return 500 if error occurs while generating all employees annual report
     */
    @Test
    void testGenerateAllEmployeesAnnualPayrollReport_InternalError() throws Exception {
        when(reportService.generateAllEmployeesAnnualPayrollReport(anyInt(), anyString()))
                .thenThrow(new RuntimeException("Internal error"));

        ResponseEntity<?> response = reportController.generateAllEmployeesAnnualPayrollReport(TITLE, YEAR);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}
