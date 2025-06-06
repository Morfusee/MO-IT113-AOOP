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

    public byte[] generateReport(List<?> data, Map<String, Object> parameters, String reportFileName)
            throws JRException, FileNotFoundException {
        // Load the .jrxml file (or .jasper if pre-compiled)
        File file = ResourceUtils.getFile("classpath:reports/" + reportFileName + ".jrxml");
        InputStream inputStream = new FileInputStream(file);

        // Compile the report if it's a .jrxml file. If it's already .jasper, skip this.
        JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);

        // Use the new DynamicJRDataSource to handle various data structures
        JRDataSource dynamicDataSource = new DynamicJRDataSource(data);

        // Fill the report with data and parameters
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dynamicDataSource);

        // Export the report to PDF (or other formats like HTML, XLS)
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        JRPdfExporter exporter = new JRPdfExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(byteArrayOutputStream));
        SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
        exporter.setConfiguration(configuration);
        exporter.exportReport();

        return byteArrayOutputStream.toByteArray();
    }

    public byte[] generatePayrollReport(String userId, LocalDate startDate, LocalDate endDate, String title)
            throws JRException, FileNotFoundException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date are required for payroll report.");
        }

        Map<String, Object> parameters = new HashMap<>(
                Map.of("ReportTitle", Objects.requireNonNullElse(title, "Payroll Report")));

        Long employeeNum = prepareCommonEmployeeParameters(userId, parameters);

        Date sqlStartDate = Date.valueOf(startDate);
        Date sqlEndDate = Date.valueOf(endDate);

        PayrollDTO payrollDTO = payrollService.generatePayroll(employeeNum, sqlStartDate, sqlEndDate);

        parameters.put("PayrollPeriod", startDate.toString() + " to " + endDate.toString());

        return generateReport(List.of(payrollDTO), parameters, "payroll"); // Assuming 'payroll' is your template name

    }

    public byte[] generateAnnualPayrollReport(String userId, Integer year, String title)
            throws JRException, FileNotFoundException {
        if (year == null) {
            throw new IllegalArgumentException("Year is required for annual payroll report.");
        }

        Map<String, Object> parameters = new HashMap<>(
                Map.of("ReportTitle", Objects.requireNonNullElse(title, "Annual Payroll Report for " + year)));

        Long employeeNum = prepareCommonEmployeeParameters(userId, parameters); // Populate common params

        // Get the annual payroll data from the PayrollService
        List<PayrollDTO> annualPayrollData = payrollService.generateAnnualPayroll(employeeNum, year);

        parameters.put("ReportYear", year);

        return generateReport(annualPayrollData, parameters, "payroll");
    }

    public byte[] generateAnnualPayrollReportSummary(String userId, Integer year, String title)
            throws JRException, FileNotFoundException {
        if (year == null) {
            throw new IllegalArgumentException("Year is required for annual payroll report.");
        }

        Map<String, Object> parameters = new HashMap<>(
                Map.of("ReportTitle", Objects.requireNonNullElse(title, "Annual Payroll Report Summary for " + year)));

        Long employeeNum = prepareCommonEmployeeParameters(userId, parameters); // Populate common params

        LocalDate localStartDate = LocalDate.of(year, 1, 1);
        LocalDate localEndDate = LocalDate.of(year, 1, 1).with(TemporalAdjusters.lastDayOfYear());

        Date startDate = Date.valueOf(localStartDate);
        Date endDate = Date.valueOf(localEndDate);

        // Get the annual payroll data from the PayrollService
        PayrollDTO annualPayrollSummaryData = payrollService.generatePayroll(employeeNum, startDate, endDate);

        parameters.put("ReportYear", year);

        return generateReport(List.of(annualPayrollSummaryData), parameters, "payroll");
    }

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
            // Use your custom ResourceNotFoundException or a more specific one if available
            throw new EntityNotFoundException("Employee with ID " + employeeNum + " not found.");
        }

        parameters.put("EmployeeNumber", employeeNum);
        parameters.put("EmployeeName", employeeDetails.employee().getPersonalInfo().getFirstName() + " " +
                employeeDetails.employee().getPersonalInfo().getLastName());
        parameters.put("Position", employeeDetails.employee().getEmploymentInfo().getPosition());

        return employeeNum;
    }
}
