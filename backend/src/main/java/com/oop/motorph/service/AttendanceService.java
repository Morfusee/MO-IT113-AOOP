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

    /**
     * Retrieves all attendance records.
     * 
     * @return List of AttendanceDTOs.
     */
    public List<AttendanceDTO> getAllAttendances() {
        log.debug("Getting all attendances.");
        return attendanceRepository.findAll().stream()
                .map(attendanceDTOMapper)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves a single attendance record by ID.
     * 
     * @param attendanceId The ID of the attendance record.
     * @return AttendanceDTO if found.
     * @throws RuntimeException if attendance is not found.
     */
    public AttendanceDTO getAttendanceById(Long attendanceId) {
        return attendanceRepository.findById(attendanceId).stream()
                .map(attendanceDTOMapper)
                .collect(Collectors.toList()).stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Attendance not found."));
    }

    /**
     * Retrieves all attendance records for a specific employee.
     * 
     * @param employeeNum The employee number.
     * @return List of AttendanceDTOs.
     */
    public List<AttendanceDTO> getAttendanceByEmployeeNum(Long employeeNum) {
        return attendanceRepository.findByEmployeeNumber(employeeNum).stream()
                .map(attendanceDTOMapper)
                .collect(Collectors.toList());
    }

    /**
     * Retrieves attendance records for a specific employee within a date range.
     * 
     * @param employeeNum The employee number.
     * @param startDate   The start date of the range.
     * @param endDate     The end date of the range.
     * @return List of AttendanceDTOs.
     */
    public List<AttendanceDTO> getAttendanceByEmployeeNum(Long employeeNum, Date startDate, Date endDate) {
        return attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate, endDate).stream()
                .map(attendanceDTOMapper)
                .collect(Collectors.toList());
    }

    // Placeholder for future implementation:
    // Saving attendance records.
    // public AttendanceDTO saveAttendance(AttendanceDTO attendance) {
    // return attendanceRepository.save(attendance)
    // }

    // Placeholder for future implementation:
    // Deleting attendance records.
    // public void deleteAttendanceById(Long attendanceId) {
    // attendanceRepository.deleteById(attendanceId);
    // }
}
