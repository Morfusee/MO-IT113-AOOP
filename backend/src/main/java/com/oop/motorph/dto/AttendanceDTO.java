package com.oop.motorph.dto;

import java.sql.Date;
import java.sql.Time;

public record AttendanceDTO(
        Long employeeNumber,
        Date date,
        Time timeIn,
        Time timeOut,
        String status,
        double totalHours,
        double overtimeHours) {
}
