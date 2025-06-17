package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.dto.mapper.PayrollDTOMapper;
import com.oop.motorph.entity.Attendance;
import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.Payroll;
import com.oop.motorph.repository.AttendanceRepository;
import com.oop.motorph.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {

    @Mock
    private AttendanceRepository attendanceRepository;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private PayrollDTOMapper payrollDTOMapper;

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private PayrollService payrollService;

    private static final Long EMPLOYEE_NUMBER_VALID = 10001L;
    private static final Long EMPLOYEE_NUMBER_INVALID = 99999L;
    private static final int YEAR = 2024;
    private static final Date START_DATE = Date.valueOf("2024-01-01");
    private static final Date END_DATE = Date.valueOf("2024-01-31");
    private static final Date ATTENDANCE_DATE = Date.valueOf("2024-01-15");
    private static final java.sql.Time TIME_IN = java.sql.Time.valueOf("08:00:00");
    private static final java.sql.Time TIME_OUT = java.sql.Time.valueOf("17:00:00");
    private static final Double BASIC_SALARY = 50000.0;
    private static final Double RICE_SUBSIDY = 1000.0;
    private static final Double PHONE_ALLOWANCE = 1000.0;
    private static final Double CLOTHING_ALLOWANCE = 1000.0;
    private static final Double GROSS_SEMI_MONTHLY_RATE = 500.0;
    private static final Double HOURLY_RATE = 100.0;
    private static final Double PAYROLL_GROSS_SALARY = 50000.0;
    private static final Double PAYROLL_TOTAL_HOURS_RENDERED = 160.0;
    private static final Double PAYROLL_HOURLY_RATE = 312.50;
    private static final Double PAYROLL_TOTAL_ALLOWANCES = 3000.0;

    private Employee employee;
    private Attendance attendance;
    private PayrollDTO payrollDTO;
    private List<Attendance> attendances;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeNumber(EMPLOYEE_NUMBER_VALID);
        employee.setCompensation(new Compensation(null, BASIC_SALARY, RICE_SUBSIDY, PHONE_ALLOWANCE, CLOTHING_ALLOWANCE,
                GROSS_SEMI_MONTHLY_RATE, HOURLY_RATE));

        attendance = new Attendance(EMPLOYEE_NUMBER_VALID, ATTENDANCE_DATE, TIME_IN, TIME_OUT);
        attendances = Arrays.asList(attendance);

        payrollDTO = PayrollDTO.builder()
                .grossSalary(PAYROLL_GROSS_SALARY)
                .totalHoursRendered(PAYROLL_TOTAL_HOURS_RENDERED)
                .hourlyRate(PAYROLL_HOURLY_RATE)
                .totalAllowances(PAYROLL_TOTAL_ALLOWANCES)
                .build();
    }

    private void mockCommonPayrollGeneration() {
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER_VALID)).thenReturn(Optional.of(employee));
        when(attendanceRepository.findByEmployeeNumberAndDateBetween(eq(EMPLOYEE_NUMBER_VALID), any(Date.class),
                any(Date.class)))
                .thenReturn(attendances);
        when(payrollDTOMapper.mapToPayroll(any(Payroll.class))).thenReturn(payrollDTO);
    }

    private void assertPayrollDTOAssertions(PayrollDTO result) {
        assertNotNull(result);
        assertEquals(payrollDTO.grossSalary(), result.grossSalary());
        assertEquals(payrollDTO.totalHoursRendered(), result.totalHoursRendered());
        assertEquals(payrollDTO.hourlyRate(), result.hourlyRate());
        assertEquals(payrollDTO.totalAllowances(), result.totalAllowances());
    }

    @Test
    void testGeneratePayrollForPeriod() {
        mockCommonPayrollGeneration();

        PayrollDTO result = payrollService.generatePayrollForPeriod(EMPLOYEE_NUMBER_VALID, START_DATE, END_DATE);

        assertPayrollDTOAssertions(result);
    }

    @Test
    void testGeneratePayrollForPeriod_EmployeeNotFound() {
        when(employeeRepository.findByEmployeeNumber(EMPLOYEE_NUMBER_INVALID)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> payrollService.generatePayrollForPeriod(EMPLOYEE_NUMBER_INVALID, START_DATE, END_DATE));
    }

    @Test
    void testGenerateAnnualPayrollForEmployee() {
        List<Date> payrollDates = Arrays.asList(START_DATE);
        EmployeeDTO employeeDTO = new EmployeeDTO(employee);

        when(employeeService.getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_VALID)).thenReturn(employeeDTO);
        when(attendanceRepository.findPayrollDatesByYear(YEAR)).thenReturn(payrollDates);
        mockCommonPayrollGeneration(); // Mocks employee and attendance find, and payroll mapping

        List<PayrollDTO> results = payrollService.generateAnnualPayrollForEmployee(EMPLOYEE_NUMBER_VALID, YEAR);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertPayrollDTOAssertions(results.get(0));
    }

    @Test
    void testGenerateAnnualPayrollForEmployee_EmployeeNotFound() {
        when(employeeService.getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_INVALID)).thenReturn(null);

        assertThrows(EntityNotFoundException.class,
                () -> payrollService.generateAnnualPayrollForEmployee(EMPLOYEE_NUMBER_INVALID, YEAR));
    }

    @Test
    void testGenerateAnnualPayrollsForAllEmployees() {
        List<Long> employeeNums = Arrays.asList(EMPLOYEE_NUMBER_VALID);
        List<Date> payrollDates = Arrays.asList(START_DATE);

        when(employeeRepository.findAllEmployeeNumbers()).thenReturn(employeeNums);
        when(attendanceRepository.findPayrollDatesByYear(YEAR)).thenReturn(payrollDates);
        mockCommonPayrollGeneration();

        List<PayrollDTO> results = payrollService.generateAnnualPayrollsForAllEmployees(YEAR);

        assertNotNull(results);
        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertPayrollDTOAssertions(results.get(0));
    }

    @Test
    void testGetPayrollStartDatesForYear() {
        List<Date> expectedDates = Arrays.asList(START_DATE);
        when(attendanceRepository.findPayrollDatesByYear(YEAR)).thenReturn(expectedDates);

        List<Date> results = payrollService.getPayrollStartDatesForYear(YEAR);

        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(START_DATE, results.get(0));
    }
}