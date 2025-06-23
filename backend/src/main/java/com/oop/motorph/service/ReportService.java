package com.oop.motorph.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Date;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import com.oop.motorph.datasource.DynamicJRDataSource;
import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.PayrollDTO;

import jakarta.persistence.EntityNotFoundException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;

@Service
public class ReportService {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    PayrollService payrollService;

    /**
     * Compiles a Jasper report template and exports it as a PDF byte array.
     *
     * @param data           Data to populate the report.
     * @param parameters     Parameters to pass into the report.
     * @param reportFileName Name of the JRXML template (without extension).
     * @return The PDF report as a byte array.
     */
    public byte[] compileAndExportReport(List<?> data, Map<String, Object> parameters, String reportFileName)
            throws JRException, FileNotFoundException {

        File file = ResourceUtils.getFile("classpath:reports/" + reportFileName + ".jrxml");
        InputStream inputStream = new FileInputStream(file);

        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
        JRDataSource dynamicDataSource = new DynamicJRDataSource(data);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dynamicDataSource);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        exporter.setConfiguration(new SimplePdfExporterConfiguration());
        exporter.exportReport();

        return byteArrayOutputStream.toByteArray();
    }

    /**
     * Generates a payroll report for a specific employee over a custom date range.
     *
     * @param userId    Employee ID as string.
     * @param startDate Start date of the payroll period.
     * @param endDate   End date of the payroll period.
     * @param title     Custom report title.
     * @return PDF report as byte array.
     */
    public byte[] generateEmployeePayrollReport(String userId, LocalDate startDate, LocalDate endDate, String title)
            throws JRException, FileNotFoundException {

        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required for payroll report.");
        }

        Map<String, Object> parameters = new HashMap<>(
                Map.of("ReportTitle", Objects.requireNonNullElse(title, "Payroll Report")));

        Long employeeNum = prepareCommonEmployeeParameters(userId, parameters);

        PayrollDTO payrollDTO = payrollService.generatePayrollForPeriod(
                employeeNum,
                Date.valueOf(startDate),
                Date.valueOf(endDate));

        parameters.put("PayrollPeriod", startDate + " to " + endDate);

        return compileAndExportReport(List.of(payrollDTO), parameters, "payroll");
    }

    /**
     * Generates an annual payroll report for a specific employee.
     *
     * @param userId Employee ID as string.
     * @param year   Year for which to generate the report.
     * @param title  Custom report title.
     * @return PDF report as byte array.
     */
    public byte[] generateEmployeeAnnualPayrollReport(String userId, Integer year, String title)
            throws JRException, FileNotFoundException {

        if (year == null) {
            throw new IllegalArgumentException("Year is required for annual payroll report.");
        }

        Map<String, Object> parameters = new HashMap<>(
                Map.of("ReportTitle", Objects.requireNonNullElse(title, "Annual Payroll Report for " + year)));

        Long employeeNum = prepareCommonEmployeeParameters(userId, parameters);
        List<PayrollDTO> annualPayrollData = payrollService.generateAnnualPayrollForEmployee(employeeNum, year);

        parameters.put("ReportYear", year);

        return compileAndExportReport(annualPayrollData, parameters, "annual_payroll");
    }

    /**
     * Generates a company-wide annual payroll summary report.
     *
     * @param year  Year for which to generate the report.
     * @param title Custom report title.
     * @return PDF report as byte array.
     */
    public byte[] generateAllEmployeesAnnualPayrollReport(Integer year, String title)
            throws JRException, FileNotFoundException {

        if (year == null) {
            throw new IllegalArgumentException("Year is required for annual payroll report.");
        }

        Map<String, Object> parameters = new HashMap<>(
                Map.of("ReportTitle", Objects.requireNonNullElse(title, "Annual Payroll Report Summary for " + year)));

        List<PayrollDTO> annualPayrollSummaryData = payrollService.generateAnnualPayrollsForAllEmployees(year);

        parameters.put("ReportYear", year);

        return compileAndExportReport(annualPayrollSummaryData, parameters, "annual_payroll");
    }

    /**
     * Parses and validates user ID, fetches employee details, and populates
     * standard report parameters.
     *
     * @param userId     Employee ID string.
     * @param parameters Report parameter map to populate.
     * @return Parsed employee number.
     */
    private Long prepareCommonEmployeeParameters(String userId, Map<String, Object> parameters) {
        if (userId == null || userId.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee ID is required.");
        }

        Long employeeNum;
        try {
            employeeNum = Long.parseLong(userId);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid employee ID format. Must be a number.");
        }

        EmployeeDTO employeeDetails = employeeService.getEmployeeByEmployeeNum(employeeNum);
        if (employeeDetails == null) {
            throw new EntityNotFoundException("Employee with ID " + employeeNum + " not found.");
        }

        parameters.put("EmployeeNumber", employeeNum);
        parameters.put("EmployeeName", employeeDetails.employee().getPersonalInfo().getFirstName() + " " +
                employeeDetails.employee().getPersonalInfo().getLastName());
        parameters.put("Position", employeeDetails.employee().getEmploymentInfo().getPosition());

        return employeeNum;
    }
}
