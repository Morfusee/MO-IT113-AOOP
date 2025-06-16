package com.oop.motorph.service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.dto.EmployeeDTO;
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

    @Autowired
    private EmployeeService employeeService;

    public List<PayrollDTO> generateAnnualPayrollsForAllEmployees(Integer year) {
        // Fetch all employeeNumbers
        List<Long> employeeList = employeeRepository.findAllEmployeeNumbers();

        // Get all payroll STARTING dates
        List<Date> payrollStartDates = getPayrollStartDatesForYear(year);

        // Get all the payroll data in all of the periods
        List<PayrollDTO> allPayrollPeriodsData = new ArrayList<>();

        for (Long employeeNum : employeeList) {
            for (Date sqlStartDate : payrollStartDates) {
                LocalDate startDate = sqlStartDate.toLocalDate();

                LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
                Date sqlEndDate = Date.valueOf(endDate);

                // Generate payroll for each period using the existing method
                PayrollDTO payrollDTO = generatePayrollForPeriod(employeeNum, sqlStartDate, sqlEndDate);
                allPayrollPeriodsData.add(payrollDTO);
            }
        }
        return allPayrollPeriodsData;
    }

    public PayrollDTO generatePayrollForPeriod(Long employeeNum, Date startDate, Date endDate) {
        // Get employee by employee number
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNum)
                .orElseThrow(EntityNotFoundException::new);

        // Check if employee exists
        List<Attendance> attendance = attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate,
                endDate);

        // Create payroll object
        return payrollDetailsDTOMapper.mapToPayroll(new Payroll(employee, attendance, startDate, endDate));
    }

    public List<PayrollDTO> generateAnnualPayrollForEmployee(Long employeeNum, Integer year) {

        // Fetch employee details - crucial for validation and later report parameters
        EmployeeDTO employeeDetails = employeeService.getEmployeeByEmployeeNum(employeeNum);
        if (employeeDetails == null) {
            throw new EntityNotFoundException("Employee with ID " + employeeNum + " not found.");
        }

        List<Date> payrollStartDates = getPayrollStartDatesForYear(year);
        // getPayrollDates method already throws ResourceNotFoundException if empty,
        // so no explicit check needed here unless you want different handling.

        List<PayrollDTO> allPayrollPeriodsData = new ArrayList<>();

        for (Date sqlStartDate : payrollStartDates) {
            LocalDate startDate = sqlStartDate.toLocalDate();

            LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
            Date sqlEndDate = Date.valueOf(endDate);

            // Generate payroll for each period using the existing method
            PayrollDTO payrollDTO = generatePayrollForPeriod(employeeNum, sqlStartDate, sqlEndDate);
            allPayrollPeriodsData.add(payrollDTO);
        }
        return allPayrollPeriodsData;
    }

    public List<Date> getPayrollStartDatesForYear(Integer year) {
        return attendanceRepository.findPayrollDatesByYear(year);
    }

}
