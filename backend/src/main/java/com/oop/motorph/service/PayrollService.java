package com.oop.motorph.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.dto.mapper.AttendanceAnalyticsDTOMapper;
import com.oop.motorph.dto.mapper.AttendanceDTOMapper;
import com.oop.motorph.dto.mapper.PayrollDTOMapper;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.Payroll;
import com.oop.motorph.repository.AttendanceRepository;
import com.oop.motorph.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class PayrollService {

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private PayrollDTOMapper payrollDetailsDTOMapper;

    public PayrollDTO generatePayroll(Long employeeNum, Date startDate, Date endDate) {
        // Get employee by employee number
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNum)
                .orElseThrow(EntityNotFoundException::new);

        // Check if employee exists
        List<Attendance> attendance = attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate,
                endDate);

        // Create payroll object
        return payrollDetailsDTOMapper.mapToPayroll(new Payroll(employee, attendance));
    }

    public List<Date> getPayrollDates(Integer year) {
        return attendanceRepository.findPayrollDatesByYear(year);
    }

}
