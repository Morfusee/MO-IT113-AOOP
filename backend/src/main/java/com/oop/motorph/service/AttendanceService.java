package com.oop.motorph.service;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.dto.mapper.AttendanceDTOMapper;
import com.oop.motorph.repository.AttendanceRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AttendanceService {

    @Autowired
    private AttendanceRepository attendanceRepository;
    @Autowired
    private AttendanceDTOMapper attendanceDTOMapper;

    public List<AttendanceDTO> getAllAttendances() {
        log.debug("Getting all attendances.");
        return attendanceRepository.findAll().stream().map(attendanceDTOMapper).collect(Collectors.toList());
    }

    public AttendanceDTO getAttendanceById(Long attendanceId) {
        return attendanceRepository.findById(attendanceId).stream().map(attendanceDTOMapper)
                .collect(Collectors.toList()).stream().findFirst()
                .orElseThrow(() -> new RuntimeException("Attendance not found."));
    }

    public List<AttendanceDTO> getAttendanceByEmployeeNum(Long employeeNum) {
        return attendanceRepository.findByEmployeeNumber(employeeNum).stream().map(attendanceDTOMapper)
                .collect(Collectors.toList());
    }

    public List<AttendanceDTO> getAttendanceByEmployeeNum(Long employeeNum, Date startDate, Date endDate) {
        return attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate, endDate).stream()
                .map(attendanceDTOMapper).collect(Collectors.toList());
    }

    // public AttendanceDTO saveAttendance(AttendanceDTO attendance) {
    //     return attendanceRepository.save(attendance)
    // }

    // public void deleteAttendanceById(Long attendanceId) {
    //     attendanceRepository.deleteById(attendanceId);
    // }
}
