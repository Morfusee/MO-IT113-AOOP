package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

import com.oop.motorph.dto.AttendanceAnalyticsDTO;
import com.oop.motorph.dto.mapper.AttendanceAnalyticsDTOMapper;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.repository.AttendanceRepository;

@ExtendWith(MockitoExtension.class)
public class AttendanceAnalyticsServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private AttendanceAnalyticsDTOMapper attendanceAnalyticsDTOMapper;

    @InjectMocks
    private AttendanceAnalyticsService attendanceAnalyticsService;

    private Attendance attendance1;
    private Attendance attendance2;
    private List<Attendance> attendances;
    private AttendanceAnalyticsDTO expectedDTO;

    // Test constants
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Date START_DATE = Date.valueOf("2024-02-01");
    private static final Date END_DATE = Date.valueOf("2024-02-02");
    private static final Time CHECK_IN_TIME = Time.valueOf("08:00:00");
    private static final Time CHECK_OUT_TIME = Time.valueOf("17:00:00");
    private static final double TOTAL_RENDERED_HOURS = 18.0;

    /**
     * Initializes test data before each test case.
     */
    @BeforeEach
    void setUp() {
        attendance1 = new Attendance(EMPLOYEE_NUMBER, START_DATE, CHECK_IN_TIME, CHECK_OUT_TIME);
        attendance2 = new Attendance(EMPLOYEE_NUMBER, END_DATE, CHECK_IN_TIME, CHECK_OUT_TIME);
        attendances = Arrays.asList(attendance1, attendance2);
        expectedDTO = new AttendanceAnalyticsDTO(2L, 0L, 0L, TOTAL_RENDERED_HOURS, CHECK_IN_TIME, CHECK_OUT_TIME);
    }

    /**
     * Mocks the mapper's behavior for converting attendance list to DTO.
     */
    private void mockMapperBehavior() {
        when(attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances)).thenReturn(expectedDTO);
    }

    /**
     * Asserts the content of an {@link AttendanceAnalyticsDTO}.
     */
    private void assertAttendanceAnalyticsDTO(AttendanceAnalyticsDTO result) {
        assertNotNull(result);
        assertEquals(expectedDTO.totalPresent(), result.totalPresent());
        assertEquals(expectedDTO.totalLates(), result.totalLates());
        assertEquals(expectedDTO.totalAbsent(), result.totalAbsent());
        assertEquals(expectedDTO.totalRenderedHours(), result.totalRenderedHours());
        assertEquals(expectedDTO.averageCheckIn(), result.averageCheckIn());
        assertEquals(expectedDTO.averageCheckOut(), result.averageCheckOut());
    }

    /**
     * Tests that analytics are correctly returned for all attendances.
     */
    @Test
    void testGetAllAttendanceAnalytics() {
        when(attendanceRepository.findAll()).thenReturn(attendances);
        mockMapperBehavior();

        AttendanceAnalyticsDTO result = attendanceAnalyticsService.getAllAttendanceAnalytics();

        assertAttendanceAnalyticsDTO(result);
    }

    /**
     * Tests that analytics are returned for a specific employee.
     */
    @Test
    void testGetAttendanceAnalyticsByEmployeeNum() {
        when(attendanceRepository.findByEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(attendances);
        mockMapperBehavior();

        AttendanceAnalyticsDTO result = attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(EMPLOYEE_NUMBER);

        assertAttendanceAnalyticsDTO(result);
    }

    /**
     * Tests that analytics are returned for a specific employee within a date
     * range.
     */
    @Test
    void testGetAttendanceAnalyticsByEmployeeNumAndDateRange() {
        when(attendanceRepository.findByEmployeeNumberAndDateBetween(EMPLOYEE_NUMBER, START_DATE, END_DATE))
                .thenReturn(attendances);
        mockMapperBehavior();

        AttendanceAnalyticsDTO result = attendanceAnalyticsService.getAttendanceAnalyticsByEmployeeNum(
                EMPLOYEE_NUMBER, START_DATE, END_DATE);

        assertAttendanceAnalyticsDTO(result);
    }
}
