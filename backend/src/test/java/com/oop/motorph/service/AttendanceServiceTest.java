package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.sql.Time;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.dto.mapper.AttendanceDTOMapper;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.repository.AttendanceRepository;

@ExtendWith(MockitoExtension.class)
public class AttendanceServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private AttendanceDTOMapper attendanceDTOMapper;

    @InjectMocks
    private AttendanceService attendanceService;

    // Test constants
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Date DATE_1 = Date.valueOf("2024-02-01");
    private static final Date DATE_2 = Date.valueOf("2024-02-02");
    private static final Time TIME_IN = Time.valueOf("08:00:00");
    private static final Time TIME_OUT = Time.valueOf("17:00:00");
    private static final String STATUS_PRESENT = "Present";
    private static final Double RENDERED_HOURS = 9.0;
    private static final Double OVERTIME_HOURS = 0.0;
    private static final Long ATTENDANCE_ID_VALID = 1L;
    private static final Long ATTENDANCE_ID_INVALID = 999L;

    private Attendance attendance1;
    private Attendance attendance2;
    private AttendanceDTO attendanceDTO1;
    private AttendanceDTO attendanceDTO2;
    private List<Attendance> attendances;

    /**
     * Sets up test data before each test.
     */
    @BeforeEach
    void setUp() {
        attendance1 = new Attendance(EMPLOYEE_NUMBER, DATE_1, TIME_IN, TIME_OUT);
        attendance2 = new Attendance(EMPLOYEE_NUMBER, DATE_2, TIME_IN, TIME_OUT);

        attendanceDTO1 = new AttendanceDTO(EMPLOYEE_NUMBER, DATE_1, TIME_IN, TIME_OUT, STATUS_PRESENT, RENDERED_HOURS,
                OVERTIME_HOURS);
        attendanceDTO2 = new AttendanceDTO(EMPLOYEE_NUMBER, DATE_2, TIME_IN, TIME_OUT, STATUS_PRESENT, RENDERED_HOURS,
                OVERTIME_HOURS);

        attendances = Arrays.asList(attendance1, attendance2);
    }

    /**
     * Helper to stub mapper behavior for a list of attendance entities.
     */
    private void mockAttendanceDTOMapperForList() {
        when(attendanceDTOMapper.apply(attendance1)).thenReturn(attendanceDTO1);
        when(attendanceDTOMapper.apply(attendance2)).thenReturn(attendanceDTO2);
    }

    /**
     * Helper to assert equality of two {@link AttendanceDTO} objects.
     */
    private void assertAttendanceDTO(AttendanceDTO expected, AttendanceDTO actual) {
        assertNotNull(actual);
        assertEquals(expected.employeeNumber(), actual.employeeNumber());
        assertEquals(expected.date(), actual.date());
        assertEquals(expected.timeIn(), actual.timeIn());
        assertEquals(expected.timeOut(), actual.timeOut());
        assertEquals(expected.status(), actual.status());
        assertEquals(expected.totalHours(), actual.totalHours());
        assertEquals(expected.overtimeHours(), actual.overtimeHours());
    }

    /**
     * Verifies that {@link AttendanceService#getAllAttendances()} returns all
     * records correctly.
     */
    @Test
    void testGetAllAttendances() {
        when(attendanceRepository.findAll()).thenReturn(attendances);
        mockAttendanceDTOMapperForList();

        List<AttendanceDTO> result = attendanceService.getAllAttendances();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertAttendanceDTO(attendanceDTO1, result.get(0));
        assertAttendanceDTO(attendanceDTO2, result.get(1));
    }

    /**
     * Verifies that {@link AttendanceService#getAttendanceById(Long)} returns the
     * correct record.
     */
    @Test
    void testGetAttendanceById() {
        when(attendanceRepository.findById(ATTENDANCE_ID_VALID)).thenReturn(Optional.of(attendance1));
        when(attendanceDTOMapper.apply(attendance1)).thenReturn(attendanceDTO1);

        AttendanceDTO result = attendanceService.getAttendanceById(ATTENDANCE_ID_VALID);

        assertAttendanceDTO(attendanceDTO1, result);
    }

    /**
     * Ensures that requesting an invalid attendance ID throws a
     * {@link RuntimeException}.
     */
    @Test
    void testGetAttendanceById_NotFound() {
        when(attendanceRepository.findById(ATTENDANCE_ID_INVALID)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            attendanceService.getAttendanceById(ATTENDANCE_ID_INVALID);
        });
    }

    /**
     * Verifies filtering by employee number returns correct mapped DTOs.
     */
    @Test
    void testGetAttendanceByEmployeeNum() {
        when(attendanceRepository.findByEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(attendances);
        mockAttendanceDTOMapperForList();

        List<AttendanceDTO> result = attendanceService.getAttendanceByEmployeeNum(EMPLOYEE_NUMBER);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertAttendanceDTO(attendanceDTO1, result.get(0));
        assertAttendanceDTO(attendanceDTO2, result.get(1));
    }

    /**
     * Verifies filtering by employee number and date range returns correct mapped
     * DTOs.
     */
    @Test
    void testGetAttendanceByEmployeeNumAndDateRange() {
        Date startDate = DATE_1;
        Date endDate = DATE_2;

        when(attendanceRepository.findByEmployeeNumberAndDateBetween(EMPLOYEE_NUMBER, startDate, endDate))
                .thenReturn(attendances);
        mockAttendanceDTOMapperForList();

        List<AttendanceDTO> result = attendanceService.getAttendanceByEmployeeNum(EMPLOYEE_NUMBER, startDate, endDate);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.get(0).date().compareTo(startDate) >= 0);
        assertTrue(result.get(1).date().compareTo(endDate) <= 0);
    }
}
