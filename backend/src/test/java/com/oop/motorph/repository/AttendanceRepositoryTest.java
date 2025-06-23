package com.oop.motorph.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.oop.motorph.entity.Attendance;

import jakarta.transaction.Transactional;

@DataJpaTest
public class AttendanceRepositoryTest {

    @Autowired
    private AttendanceRepository attendanceRepository;

    // Constants for reusable test data
    private static final Long EMPLOYEE_NUMBER = 10001L;
    private static final Long NON_EXISTENT_EMPLOYEE_NUMBER = 99999L;
    private static final Date DATE_2024_01_01 = Date.valueOf("2024-01-01");
    private static final Date DATE_2024_01_02 = Date.valueOf("2024-01-02");
    private static final Date FUTURE_START_DATE = Date.valueOf("2025-01-01");
    private static final Date FUTURE_END_DATE = Date.valueOf("2025-01-31");
    private static final Time START_TIME = Time.valueOf("08:00:00");
    private static final Time END_TIME = Time.valueOf("17:00:00");

    private Attendance attendance1;
    private Attendance attendance2;

    /**
     * Setup test data before each test case.
     */
    @BeforeEach
    void setUp() {
        attendanceRepository.deleteAll();

        attendance1 = new Attendance(EMPLOYEE_NUMBER, DATE_2024_01_01, START_TIME, END_TIME);
        attendance2 = new Attendance(EMPLOYEE_NUMBER, DATE_2024_01_02, START_TIME, END_TIME);

        attendanceRepository.save(attendance1);
        attendanceRepository.save(attendance2);
    }

    /**
     * Helper method to assert attendance list size and contents.
     */
    private void assertAttendanceList(List<Attendance> attendances, int expectedSize) {
        assertNotNull(attendances);
        assertEquals(expectedSize, attendances.size());
        if (expectedSize > 0) {
            assertEquals(EMPLOYEE_NUMBER, attendances.get(0).getEmployeeNumber());
        }
    }

    /**
     * Test fetching attendance by employee number.
     */
    @Test
    @Transactional
    @Rollback
    void testFindByEmployeeNumber() {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumber(EMPLOYEE_NUMBER);

        assertAttendanceList(attendances, 2);
        assertEquals(EMPLOYEE_NUMBER, attendances.get(1).getEmployeeNumber());
    }

    /**
     * Test fetching attendance records between two dates.
     */
    @Test
    @Transactional
    @Rollback
    void testFindByEmployeeNumberAndDateBetween() {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumberAndDateBetween(
                EMPLOYEE_NUMBER, DATE_2024_01_01, DATE_2024_01_02);

        assertAttendanceList(attendances, 2);
        assertTrue(attendances.get(0).getDate().compareTo(DATE_2024_01_01) >= 0);
        assertTrue(attendances.get(1).getDate().compareTo(DATE_2024_01_02) <= 0);
    }

    /**
     * Test finding distinct payroll dates by year.
     */
    @Test
    @Transactional
    @Rollback
    void testFindPayrollDatesByYear() {
        List<Date> payrollDates = attendanceRepository.findPayrollDatesByYear(2024);

        assertNotNull(payrollDates);
        assertFalse(payrollDates.isEmpty());
        assertEquals(DATE_2024_01_01, payrollDates.get(0));
    }

    /**
     * Test behavior when querying an employee number with no records.
     */
    @Test
    @Transactional
    @Rollback
    void testFindByEmployeeNumber_NoResults() {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumber(NON_EXISTENT_EMPLOYEE_NUMBER);
        assertAttendanceList(attendances, 0);
    }

    /**
     * Test querying date ranges with no attendance records.
     */
    @Test
    @Transactional
    @Rollback
    void testFindByEmployeeNumberAndDateBetween_NoResults() {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumberAndDateBetween(
                EMPLOYEE_NUMBER, FUTURE_START_DATE, FUTURE_END_DATE);
        assertAttendanceList(attendances, 0);
    }

    /**
     * Test payroll date lookup for a year with no attendance records.
     */
    @Test
    @Transactional
    @Rollback
    void testFindPayrollDatesByYear_NoResults() {
        List<Date> payrollDates = attendanceRepository.findPayrollDatesByYear(2025);
        assertNotNull(payrollDates);
        assertTrue(payrollDates.isEmpty());
    }
}
