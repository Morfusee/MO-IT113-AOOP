package com.oop.motorph.dto;

import java.sql.Time;

/**
 * A Data Transfer Object (DTO) for conveying attendance analytics.
 * This record class encapsulates key metrics derived from attendance records,
 * providing a summarized view of an employee's attendance performance.
 *
 * @param totalPresent       The total number of days an employee was marked
 *                           present.
 * @param totalLates         The total number of instances an employee was late.
 * @param totalAbsent        The total number of days an employee was absent.
 * @param totalRenderedHours The total hours an employee has worked.
 * @param averageCheckIn     The average check-in time of an employee.
 * @param averageCheckOut    The average check-out time of an employee.
 */
public record AttendanceAnalyticsDTO(
        Long totalPresent,
        Long totalLates,
        Long totalAbsent,
        Double totalRenderedHours,
        Time averageCheckIn,
        Time averageCheckOut) {
}