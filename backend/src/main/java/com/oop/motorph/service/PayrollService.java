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

    /**
     * Generates payrolls for all employees for the entire specified year.
     *
     * @param year The target year.
     * @return List of PayrollDTOs representing each employee’s monthly payroll.
     */
    public List<PayrollDTO> generateAnnualPayrollsForAllEmployees(Integer year) {
        List<Long> employeeList = employeeRepository.findAllEmployeeNumbers();
        List<Date> payrollStartDates = getPayrollStartDatesForYear(year);
        List<PayrollDTO> allPayrollPeriodsData = new ArrayList<>();

        for (Long employeeNum : employeeList) {
            for (Date sqlStartDate : payrollStartDates) {
                LocalDate startDate = sqlStartDate.toLocalDate();
                LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
                Date sqlEndDate = Date.valueOf(endDate);

                PayrollDTO payrollDTO = generatePayrollForPeriod(employeeNum, sqlStartDate, sqlEndDate);
                allPayrollPeriodsData.add(payrollDTO);
            }
        }
        return allPayrollPeriodsData;
    }

    /**
     * Generates a payroll for a specific employee for a specific date range.
     *
     * @param employeeNum The employee number.
     * @param startDate   Start date of the payroll period.
     * @param endDate     End date of the payroll period.
     * @return A PayrollDTO representing the computed payroll.
     */
    public PayrollDTO generatePayrollForPeriod(Long employeeNum, Date startDate, Date endDate) {
        Employee employee = employeeRepository.findByEmployeeNumber(employeeNum)
                .orElseThrow(EntityNotFoundException::new);

        List<Attendance> attendance = attendanceRepository.findByEmployeeNumberAndDateBetween(employeeNum, startDate,
                endDate);

        return payrollDetailsDTOMapper.mapToPayroll(new Payroll(employee, attendance, startDate, endDate));
    }

    /**
     * Generates payrolls for a specific employee for all months of the specified
     * year.
     *
     * @param employeeNum The employee number.
     * @param year        The target year.
     * @return List of PayrollDTOs for each month of the year.
     */
    public List<PayrollDTO> generateAnnualPayrollForEmployee(Long employeeNum, Integer year) {
        EmployeeDTO employeeDetails = employeeService.getEmployeeByEmployeeNum(employeeNum);
        if (employeeDetails == null) {
            throw new EntityNotFoundException("Employee with ID " + employeeNum + " not found.");
        }

        List<Date> payrollStartDates = getPayrollStartDatesForYear(year);
        List<PayrollDTO> allPayrollPeriodsData = new ArrayList<>();

        for (Date sqlStartDate : payrollStartDates) {
            LocalDate startDate = sqlStartDate.toLocalDate();
            LocalDate endDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
            Date sqlEndDate = Date.valueOf(endDate);

            PayrollDTO payrollDTO = generatePayrollForPeriod(employeeNum, sqlStartDate, sqlEndDate);
            allPayrollPeriodsData.add(payrollDTO);
        }

        return allPayrollPeriodsData;
    }

    /**
     * Retrieves the list of payroll start dates (first of each month) for the given
     * year.
     *
     * @param year The target year.
     * @return List of payroll period start dates.
     */
    public List<Date> getPayrollStartDatesForYear(Integer year) {
        return attendanceRepository.findPayrollDatesByYear(year);
    }

}
