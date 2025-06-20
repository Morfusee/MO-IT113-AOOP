package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.service.PayrollService;
import com.oop.motorph.utils.ApiResponse;

@ExtendWith(MockitoExtension.class)
public class PayrollControllerTest {

    @InjectMocks
    private PayrollController payrollController;

    @Mock
    private PayrollService payrollService;

    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final String START_DATE = "2024-01-01";
    private static final String END_DATE = "2024-01-31";
    private static final Integer YEAR = 2024;

    private PayrollDTO mockPayrollDTO;
    private List<Date> mockPayrollDates;

    @BeforeEach
    void setup() {
        // Setup mock payroll data
        mockPayrollDTO = PayrollDTO.builder()
                .employeeNum(EMPLOYEE_NUMBER)
                .startDate(Date.valueOf(START_DATE))
                .endDate(Date.valueOf(END_DATE))
                .build();

        mockPayrollDates = Arrays.asList(
                Date.valueOf(START_DATE),
                Date.valueOf(END_DATE));
    }

    private void assertOkResponse(ResponseEntity<ApiResponse<?>> response, String expectedMessage) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody() != null, "Response body should not be null");
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    /**
     * Test: Successfully generate and fetch payroll data for a given employee and date range.
     */
    @Test
    void testGetPayroll_Success() {
        when(payrollService.generatePayrollForPeriod(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(mockPayrollDTO);

        ResponseEntity<ApiResponse<?>> response = payrollController.getPayroll(
                EMPLOYEE_NUMBER,
                START_DATE,
                END_DATE);

        assertOkResponse(response, "Fetched payroll successfully.");
    }

    /**
     * Test: Handle error if payroll generation fails (e.g., due to backend error).
     */
    @Test
    void testGetPayroll_Error() {
        when(payrollService.generatePayrollForPeriod(anyLong(), any(Date.class), any(Date.class)))
                .thenThrow(new RuntimeException("Error generating payroll"));

        ResponseEntity<ApiResponse<?>> response = payrollController.getPayroll(
                EMPLOYEE_NUMBER,
                START_DATE,
                END_DATE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully retrieve payroll start dates (pay periods) for a given year.
     */
    @Test
    void testGetPayrollDates_Success() {
        when(payrollService.getPayrollStartDatesForYear(anyInt())).thenReturn(mockPayrollDates);

        ResponseEntity<ApiResponse<?>> response = payrollController.getPayrollDates(YEAR);

        assertOkResponse(response, "Fetched payroll months successfully.");
    }

    /**
     * Test: Handle error if payroll start dates retrieval fails (e.g., invalid year).
     */
    @Test
    void testGetPayrollDates_Error() {
        when(payrollService.getPayrollStartDatesForYear(anyInt()))
                .thenThrow(new RuntimeException("Error fetching payroll dates"));

        ResponseEntity<ApiResponse<?>> response = payrollController.getPayrollDates(YEAR);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
