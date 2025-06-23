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
public class HRPayrollControllerTest {

    @InjectMocks
    private HRPayrollController hrPayrollController;

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
        // Initialize mock payroll DTO
        mockPayrollDTO = PayrollDTO.builder()
                .employeeNum(EMPLOYEE_NUMBER)
                .startDate(Date.valueOf(START_DATE))
                .endDate(Date.valueOf(END_DATE))
                .build();

        // Mock payroll start dates
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
     * Test: Successfully fetch payroll for an employee for a given date range.
     */
    @Test
    void testGetPayroll_Success() {
        when(payrollService.generatePayrollForPeriod(anyLong(), any(Date.class), any(Date.class)))
                .thenReturn(mockPayrollDTO);

        ResponseEntity<ApiResponse<?>> response = hrPayrollController.getPayroll(
                EMPLOYEE_NUMBER,
                START_DATE,
                END_DATE);

        assertOkResponse(response, "Fetched payroll successfully.");
    }

    /**
     * Test: Error handling when payroll generation fails.
     */
    @Test
    void testGetPayroll_Error() {
        when(payrollService.generatePayrollForPeriod(anyLong(), any(Date.class), any(Date.class)))
                .thenThrow(new RuntimeException("Error generating payroll"));

        ResponseEntity<ApiResponse<?>> response = hrPayrollController.getPayroll(
                EMPLOYEE_NUMBER,
                START_DATE,
                END_DATE);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    /**
     * Test: Successfully fetch payroll start dates for a specific year.
     */
    @Test
    void testGetPayrollDates_Success() {
        when(payrollService.getPayrollStartDatesForYear(anyInt())).thenReturn(mockPayrollDates);

        ResponseEntity<ApiResponse<?>> response = hrPayrollController.getPayrollDates(YEAR);

        assertOkResponse(response, "Fetched payroll months successfully.");
    }

    /**
     * Test: Error handling when fetching payroll start dates fails.
     */
    @Test
    void testGetPayrollDates_Error() {
        when(payrollService.getPayrollStartDatesForYear(anyInt()))
                .thenThrow(new RuntimeException("Error fetching payroll dates"));

        ResponseEntity<ApiResponse<?>> response = hrPayrollController.getPayrollDates(YEAR);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
