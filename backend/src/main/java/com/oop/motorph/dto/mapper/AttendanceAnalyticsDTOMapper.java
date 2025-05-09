package com.oop.motorph.dto.mapper;

import java.util.List;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceAnalyticsDTO;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.entity.AttendanceAnalytics;

@Service
public class AttendanceAnalyticsDTOMapper {
    public AttendanceAnalyticsDTO mapToAnalyticsDTO(List<Attendance> attendances) {

        AttendanceAnalytics analytics = new AttendanceAnalytics();

        return new AttendanceAnalyticsDTO(
                analytics.calculateTotalPresent(attendances),
                analytics.calculateTotalLates(attendances),
                analytics.calculateTotalAbsent(attendances),
                analytics.calculateTotalRenderedHours(attendances),
                analytics.calculateAverageCheckIn(attendances),
                analytics.calculateAverageCheckOut(attendances));
    }
}
