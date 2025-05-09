package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.entity.Attendance;

@Service
public class AttendanceDTOMapper implements Function<Attendance, AttendanceDTO> {
    @Override
    public AttendanceDTO apply(Attendance attendance) {
        return new AttendanceDTO(attendance.getEmployeeNumber(), attendance.getDate(), attendance.getTimeIn(),
                attendance.getTimeOut(), attendance.getStatus(), attendance.calculateTotalHours(),
                attendance.calculateOvertime());
    }
}
