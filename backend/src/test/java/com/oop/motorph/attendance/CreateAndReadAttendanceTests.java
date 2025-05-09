package com.oop.motorph.attendance;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;

import com.oop.motorph.entity.Attendance;
import com.oop.motorph.utils.AttendanceTestUtils;

@DataJpaTest
@ComponentScan(basePackages = { "com.oop.motorph" })
public class CreateAndReadAttendanceTests {
    @Autowired
    private AttendanceTestUtils attendanceTestUtils;

    private Attendance savedAttendance;

    @BeforeEach
    public void setup() {
        savedAttendance = attendanceTestUtils.createAndSaveAttendance();
    }

    @Test
    public void testEmployeeNumber() {
        assertEquals(savedAttendance.getEmployeeNumber(), 10001L);
    }

    @Test
    public void testDate() {
        assertEquals(savedAttendance.getDate(), attendanceTestUtils.convertStringToSqlDate("2022-09-01", "yyyy-MM-dd"));
    }

    @Test
    public void testTimeIn() {
        assertEquals(savedAttendance.getTimeIn(), attendanceTestUtils.convertStringToSqlTime("08:00", "HH:mm"));
    }

    @Test
    public void testTimeOut() {
        assertEquals(savedAttendance.getTimeOut(), attendanceTestUtils.convertStringToSqlTime("17:00", "HH:mm"));
    }

    @Test
    public void testStatus() {
        assertEquals(savedAttendance.getStatus(), "Present");
    }

    @Test
    public void testOvertime() {
        assertEquals(savedAttendance.getOvertimeHours(), 0);
    }

    @Test
    public void testTotalHours() {
        assertEquals(savedAttendance.getTotalHours(), 9);
    }
}
