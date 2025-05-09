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

    public AttendanceAnalyticsDTO getAllAttendanceAnalytics() {
        List<Attendance> attendances = attendanceRepository.findAll();

        return attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances);
    }

    public AttendanceAnalyticsDTO getAttendanceAnalyticsByEmployeeNum(Long employeeNum) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumber(employeeNum);

        return attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances);
    }

    public AttendanceAnalyticsDTO getAttendanceAnalyticsByEmployeeNum(Long employeeNum, Date startDate, Date endDate) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate,
                endDate);

        return attendanceAnalyticsDTOMapper.mapToAnalyticsDTO(attendances);
    }

}
