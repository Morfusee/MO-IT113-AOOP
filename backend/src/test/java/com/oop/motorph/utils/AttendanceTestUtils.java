package com.oop.motorph.utils;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oop.motorph.entity.Attendance;
import com.oop.motorph.repository.AttendanceRepository;

@Component
public class AttendanceTestUtils {
    @Autowired
    private AttendanceRepository attendanceRepository;

    public Date convertStringToSqlDate(String dateString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate localDate = LocalDate.parse(dateString, formatter);
            return Date.valueOf(localDate);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid date format: " + e.getMessage());
            return null; // Or throw an exception

        }
    }

    public Time convertStringToSqlTime(String timeString, String pattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
            LocalTime localTime = LocalTime.parse(timeString, formatter);
            return Time.valueOf(localTime);
        } catch (DateTimeParseException e) {
            System.err.println("Invalid time format: " + e.getMessage());
            return null; // Or throw an exception
        }
    }

    public Attendance createAndSaveAttendance() {
        Attendance attendance = new Attendance(10001L,
                convertStringToSqlDate("2022-09-01", "yyyy-MM-dd"),
                convertStringToSqlTime("08:00", "HH:mm"),
                convertStringToSqlTime("17:00", "HH:mm"));

        return attendanceRepository.save(attendance);
    }
}
