package com.oop.motorph;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.oop.motorph.dto.mapper.AttendanceDTOMapper;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.repository.AttendanceRepository;
import com.oop.motorph.utils.AttendanceTestUtils;

import jakarta.transaction.Transactional;

@DataJpaTest
public class AttendanceRepositoryTests {
    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceDTOMapper attendanceDTOMapper;

    private Attendance savedAttendance;

    @BeforeEach
    @Transactional
    @Rollback
    public void setup() {
        Attendance attendance = new Attendance(10001L,
                AttendanceTestUtils.convertStringToSqlDate("2022-09-01", "yyyy-MM-dd"),
                AttendanceTestUtils.convertStringToSqlTime("08:00", "HH:mm"),
                AttendanceTestUtils.convertStringToSqlTime("16:00", "HH:mm"));

        savedAttendance = attendanceRepository.save(attendance);
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateAttendance() {
        assertEquals(savedAttendance.getEmployeeNumber(), 10001L);
        assertEquals(savedAttendance.getDate(), AttendanceTestUtils.convertStringToSqlDate("2022-09-01", "yyyy-MM-dd"));
        assertEquals(savedAttendance.getTimeIn(), AttendanceTestUtils.convertStringToSqlTime("08:00", "HH:mm"));
        assertEquals(savedAttendance.getTimeOut(), AttendanceTestUtils.convertStringToSqlTime("16:00", "HH:mm"));
        assertEquals(savedAttendance.getStatus(), "Present");
    }
}
