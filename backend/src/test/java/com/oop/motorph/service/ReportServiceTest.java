package com.oop.motorph.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.PayrollDTO;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.PersonalInfo;
import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.JRException;

@ExtendWith(MockitoExtension.class)
public class ReportServiceTest {

    @Mock
    private EmployeeService employeeService;

    @Mock
    private PayrollService payrollService;

    @InjectMocks
    private ReportService reportService;

    // Constants for test data
    private static final Long EMPLOYEE_NUMBER_VALID = 10001L;
    private static final String EMPLOYEE_NUMBER_VALID_STR = "10001";
    private static final Long EMPLOYEE_NUMBER_INVALID = 99999L;
    private static final String EMPLOYEE_NUMBER_INVALID_STR = "99999";
    private static final String INVALID_USER_ID_STR = "invalid";
    private static final int YEAR = 2024;
    private static final String LAST_NAME = "Doe";
    private static final String FIRST_NAME = "John";
    private static final String BIRTHDATE = "1990-01-01";
    private static final String ADDRESS = "123 Street";
    private static final String PHONE_NUMBER = "1234567890";
    private static final String EMPLOYMENT_STATUS = "Regular";
    private static final String POSITION = "Developer";
    private static final String DEPARTMENT = "IT Department";
    private static final Double GROSS_SALARY = 50000.0;
    private static final Double TOTAL_HOURS_RENDERED = 160.0;
    private static final Double HOURLY_RATE = 312.50;
    private static final Double TOTAL_ALLOWANCES = 3000.0;
    private static final LocalDate START_DATE = LocalDate.of(2024, 1, 1);
    private static final LocalDate END_DATE = LocalDate.of(2024, 1, 31);
    private static final String REPORT_NAME = "Test Report";
    private static final String ANNUAL_REPORT_NAME = "Annual Report";
    private static final String SUMMARY_REPORT_NAME = "Summary Report";

    private Employee employee;
    private EmployeeDTO employeeDTO;
    private PayrollDTO payrollDTO;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setEmployeeNumber(EMPLOYEE_NUMBER_VALID);
        employee.setPersonalInfo(new PersonalInfo(LAST_NAME, FIRST_NAME, BIRTHDATE, ADDRESS, PHONE_NUMBER));
        employee.setEmploymentInfo(new EmploymentInfo(EMPLOYMENT_STATUS, POSITION, DEPARTMENT));

        employeeDTO = new EmployeeDTO(employee);

        payrollDTO = PayrollDTO.builder()
                .grossSalary(GROSS_SALARY)
                .totalHoursRendered(TOTAL_HOURS_RENDERED)
                .hourlyRate(HOURLY_RATE)
                .totalAllowances(TOTAL_ALLOWANCES)
                .build();
    }

    private void mockPayrollServiceGeneratePayrollForPeriod() {
        when(payrollService.generatePayrollForPeriod(eq(EMPLOYEE_NUMBER_VALID), any(Date.class), any(Date.class)))
                .thenReturn(payrollDTO);
    }

    private void mockEmployeeServiceGetEmployeeByEmployeeNum() {
        when(employeeService.getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_VALID)).thenReturn(employeeDTO);
    }

    @Test
    void testGenerateEmployeePayrollReport() throws JRException, FileNotFoundException {
        mockEmployeeServiceGetEmployeeByEmployeeNum();
        mockPayrollServiceGeneratePayrollForPeriod();

        byte[] result = reportService.generateEmployeePayrollReport(EMPLOYEE_NUMBER_VALID_STR, START_DATE, END_DATE,
                REPORT_NAME);

        assertNotNull(result);
        verify(employeeService).getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_VALID);
        verify(payrollService).generatePayrollForPeriod(eq(EMPLOYEE_NUMBER_VALID), any(Date.class), any(Date.class));
    }

    @Test
    void testGenerateEmployeePayrollReport_NullDates() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .generateEmployeePayrollReport(EMPLOYEE_NUMBER_VALID_STR, null, null, REPORT_NAME));
    }

    @Test
    void testGenerateEmployeePayrollReport_InvalidUserId() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .generateEmployeePayrollReport(INVALID_USER_ID_STR, START_DATE, END_DATE, REPORT_NAME));
    }

    @Test
    void testGenerateEmployeePayrollReport_EmployeeNotFound() {
        when(employeeService.getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_INVALID)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> reportService
                .generateEmployeePayrollReport(EMPLOYEE_NUMBER_INVALID_STR, START_DATE, END_DATE, REPORT_NAME));
    }

    @Test
    void testGenerateEmployeeAnnualPayrollReport() throws JRException, FileNotFoundException {
        List<PayrollDTO> annualPayroll = Arrays.asList(payrollDTO);
        mockEmployeeServiceGetEmployeeByEmployeeNum();
        when(payrollService.generateAnnualPayrollForEmployee(EMPLOYEE_NUMBER_VALID, YEAR))
                .thenReturn(annualPayroll);

        byte[] result = reportService.generateEmployeeAnnualPayrollReport(EMPLOYEE_NUMBER_VALID_STR, YEAR,
                ANNUAL_REPORT_NAME);

        assertNotNull(result);
        verify(employeeService).getEmployeeByEmployeeNum(EMPLOYEE_NUMBER_VALID);
        verify(payrollService).generateAnnualPayrollForEmployee(EMPLOYEE_NUMBER_VALID, YEAR);
    }

    @Test
    void testGenerateEmployeeAnnualPayrollReport_NullYear() {
        assertThrows(IllegalArgumentException.class, () -> reportService
                .generateEmployeeAnnualPayrollReport(EMPLOYEE_NUMBER_VALID_STR, null, ANNUAL_REPORT_NAME));
    }

    @Test
    void testGenerateAllEmployeesAnnualPayrollReport() throws JRException, FileNotFoundException {
        List<PayrollDTO> annualPayrollSummary = Arrays.asList(payrollDTO);
        when(payrollService.generateAnnualPayrollsForAllEmployees(YEAR)).thenReturn(annualPayrollSummary);

        byte[] result = reportService.generateAllEmployeesAnnualPayrollReport(YEAR, SUMMARY_REPORT_NAME);

        assertNotNull(result);
        verify(payrollService).generateAnnualPayrollsForAllEmployees(YEAR);
    }

    @Test
    void testGenerateAllEmployeesAnnualPayrollReport_NullYear() {
        assertThrows(IllegalArgumentException.class,
                () -> reportService.generateAllEmployeesAnnualPayrollReport(null, SUMMARY_REPORT_NAME));
    }
}