package com.oop.motorph.dto;

import java.sql.Time;

public record AttendanceAnalyticsDTO(
                Long totalPresent,
                Long totalLates,
                Long totalAbsent,
                Double totalRenderedHours,
                Time averageCheckIn,
                Time averageCheckOut) {
}
