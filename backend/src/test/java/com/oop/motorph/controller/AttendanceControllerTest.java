package com.oop.motorph.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.sql.Time;
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

import com.oop.motorph.dto.AttendanceAnalyticsDTO;
import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.service.AttendanceAnalyticsService;
import com.oop.motorph.service.AttendanceService;
import com.oop.motorph.utils.ApiResponse;

@ExtendWith(MockitoExtension.class)
public class AttendanceControllerTest {

    @InjectMocks
    private AttendanceController attendanceController;

    @Mock
    private AttendanceService attendanceService;

    @Mock
    private AttendanceAnalyticsService attendanceAnalyticsService;

    // Constants for test data
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Long ATTENDANCE_ID = 1L;
    private static final Date DATE = Date.valueOf("2024-01-01");
    private static final Time TIME_IN = Time.valueOf("08:00:00");
    private static final Time TIME_OUT = Time.valueOf("17:00:00");
    private static final String ATTENDANCE_STATUS = "Present";
    private static final Double RENDERED_HOURS = 9.0;
    private static final Double OVERTIME_HOURS = 0.0;
    private static final String START_DATE_STRING = "2024-01-01";
    private static final String END_DATE_STRING = "2024-01-31";

    // Constants for AnalyticsDTO
    private static final Long TOTAL_DAYS_PRESENT = 20L;
    private static final Long TOTAL_DAYS_ABSENT = 2L;
    private static final Long TOTAL_DAYS_LATE = 1L;
    private static final Double TOTAL_HOURS_RENDERED_ANALYTICS = 160.0;
    private static final Time AVERAGE_TIME_IN = Time.valueOf("08:00:00");
    private static final Time AVERAGE_TIME_OUT = Time.valueOf("17:00:00");

    private AttendanceDTO mockAttendance;
    private List<AttendanceDTO> mockAttendanceList;
    private AttendanceAnalyticsDTO mockAnalytics;

    @BeforeEach
    void setup() {
        mockAttendance = new AttendanceDTO(
                EMPLOYEE_NUMBER,
                DATE,
                TIME_IN,
                TIME_OUT,
                ATTENDANCE_STATUS,
                RENDERED_HOURS,
                OVERTIME_HOURS);

        mockAttendanceList = Arrays.asList(mockAttendance);

        mockAnalytics = new AttendanceAnalyticsDTO(
                TOTAL_DAYS_PRESENT,
                TOTAL_DAYS_ABSENT,
                TOTAL_DAYS_LATE,
                TOTAL_HOURS_RENDERED_ANALYTICS,
                AVERAGE_TIME_IN,
                AVERAGE_TIME_OUT);
    }

    /**
     * Asserts a successful HTTP response with expected message and 200 OK status.
     */
    @SuppressWarnings("null")
    private void assertOkResponse(ResponseEntity<ApiResponse<?>> response, String expectedMessage) {
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(true, response.getBody() != null, "Response body should not be null");
        assertEquals(expectedMessage, response.getBody().getMessage());
    }

    /**
     * Tests fetching all attendances for a given employee without date range.
     */
    @Test
    @SuppressWarnings("null")
    void testGetAttendances_Success() {
        when(attendanceService.getAttendanceByEmployeeNum(anyLong())).thenReturn(mockAttendanceList);

        ResponseEntity<ApiResponse<?>> response = attendanceController.getAttendances(EMPLOYEE_NUMBER, null, null);

        assertOkResponse(response, "Fetched employee attendances successfully.");
    }

    /**
     * Tests fetching attendances for a given employee with a date range.
     */
    @Test
    @SuppressWarnings("null")
    void testGetAttendances_WithDateRange_Success() {
        when(attendanceService.getAttendanceByEmployeeNum(anyLong(), any(), any())).thenReturn(mockAttendanceList);

        ResponseEntity<ApiResponse<?>> response = attendanceController.getAttendances(
                EMPLOYEE_NUMBER,
                START_DATE_STRING,
                END_DATE_STRING);

        assertOkResponse(response, "Fetched employee attendances successfully.");
    }

    /**
     * Tests fetching a specific attendance record by its ID.
     */
    @Test
    @SuppressWarnings("null")
    void testGetAttendanceById_Success() {
        when(attendanceService.getAttendanceById(anyLong())).thenReturn(mockAttendance);

        ResponseEntity<ApiResponse<?>> response = attendanceController.getAttendanceById(ATTENDANCE_ID);

        assertOkResponse(response, "Attendance fetched successfully.");
    }

    /**
     * Tests fetching attendance analytics for an employee without a date range.
     */
    @Test
    @SuppressWarnings("null")
    void testGetAttendanceAnalytics_Success() {
        when(attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(anyLong())).thenReturn(mockAnalytics);

        ResponseEntity<ApiResponse<?>> response = attendanceController.getAttendancesAnalytics(
                EMPLOYEE_NUMBER,
                null,
                null);

        assertOkResponse(response, "Fetched all attendance analytics successfully.");
    }

    /**
     * Tests fetching attendance analytics for an employee within a specific date range.
     */
    @Test
    @SuppressWarnings("null")
    void testGetAttendanceAnalytics_WithDateRange_Success() {
        when(attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(anyLong(), any(), any()))
                .thenReturn(mockAnalytics);

        ResponseEntity<ApiResponse<?>> response = attendanceController.getAttendancesAnalytics(
                EMPLOYEE_NUMBER,
                START_DATE_STRING,
                END_DATE_STRING);

        assertOkResponse(response, "Fetched all attendance analytics successfully.");
    }
}
