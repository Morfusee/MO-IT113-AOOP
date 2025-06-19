package com.oop.motorph.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceAnalyticsDTO;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.entity.AttendanceAnalytics;

/**
 * Service class responsible for mapping a list of {@link Attendance} entities
 * to an {@link AttendanceAnalyticsDTO}. It uses the business logic defined
 * in {@link AttendanceAnalytics} to compute various attendance metrics.
 */
@Service
public class AttendanceAnalyticsDTOMapper {

    /**
     * Maps a list of Attendance entities to an AttendanceAnalyticsDTO.
     * This method calculates key attendance metrics such as total present,
     * total lates, total absent, total rendered hours, average check-in,
     * and average check-out based on the provided attendance records.
     *
     * @param attendances A list of {@link Attendance} entities from which to
     *                    calculate analytics.
     * @return An {@link AttendanceAnalyticsDTO} containing the computed attendance
     *         metrics.
     */
    public AttendanceAnalyticsDTO mapToAnalyticsDTO(List<Attendance> attendances) {

        // Create an instance of AttendanceAnalytics to perform calculations.
        AttendanceAnalytics analytics = new AttendanceAnalytics();

        // Construct and return a new AttendanceAnalyticsDTO with the calculated values.
        return new AttendanceAnalyticsDTO(
                analytics.calculateTotalPresent(attendances),
                analytics.calculateTotalLates(attendances),
                analytics.calculateTotalAbsent(attendances),
                analytics.calculateTotalRenderedHours(attendances),
                analytics.calculateAverageCheckIn(attendances),
                analytics.calculateAverageCheckOut(attendances));
    }
}