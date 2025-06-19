package com.oop.motorph.dto;

import java.sql.Date;
import java.sql.Time;

/**
 * A Data Transfer Object (DTO) for conveying individual attendance records.
 * This record class encapsulates an employee's daily attendance details,
 * including calculated total hours worked and overtime.
 *
 * @param employeeNumber The unique identifier of the employee.
 * @param date           The date of the attendance record.
 * @param timeIn         The time the employee checked in.
 * @param timeOut        The time the employee checked out.
 * @param status         The attendance status (e.g., "Present", "Absent",
 *                       "Late").
 * @param totalHours     The total hours worked for the day.
 * @param overtimeHours  The overtime hours accumulated for the day.
 */
public record AttendanceDTO(
                Long employeeNumber,
                Date date,
                Time timeIn,
                Time timeOut,
                String status,
                double totalHours,
                double overtimeHours) {
}