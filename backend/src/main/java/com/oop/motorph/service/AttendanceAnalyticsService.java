package com.oop.motorph.service;

import java.sql.Date;
import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceAnalyticsDTO;
import com.oop.motorph.dto.mapper.AttendanceAnalyticsDTOMapper;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.entity.AttendanceAnalytics;
import com.oop.motorph.repository.AttendanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceAnalyticsService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private AttendanceAnalyticsDTOMapper attendanceAnalyticsDTOMapper;

    /**
     * Retrieves analytics for all attendance records.
     * 
     * @return AttendanceAnalyticsDTO containing aggregated data.
     */
    public AttendanceAnalyticsDTO getAllAttendanceAnalytics() {
        List<Attendance> attendances = attendanceRepository.findAll();
        return attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances);
    }

    /**
     * Retrieves analytics for a specific employee's attendance.
     * 
     * @param employeeNum The employee number.
     * @return AttendanceAnalyticsDTO for the employee.
     */
    public AttendanceAnalyticsDTO getAttendanceAnalyticsByEmployeeNum(Long employeeNum) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumber(employeeNum);
        return attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances);
    }

    /**
     * Retrieves analytics for a specific employee's attendance within a date range.
     * 
     * @param employeeNum The employee number.
     * @param startDate   The start date of the range.
     * @param endDate     The end date of the range.
     * @return AttendanceAnalyticsDTO for the filtered records.
     */
    public AttendanceAnalyticsDTO getAttendanceAnalyticsByEmployeeNum(Long employeeNum, Date startDate, Date endDate) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate,
                endDate);
        return attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances);
    }

}
