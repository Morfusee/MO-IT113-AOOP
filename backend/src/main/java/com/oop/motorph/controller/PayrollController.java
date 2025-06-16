package com.oop.motorph.controller;

import java.sql.Date;
import java.util.Map;

import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.oop.motorph.service.PayrollService;
import com.oop.motorph.utils.ApiResponse;

@RestController
@CrossOrigin(origins = "http://localhost:5173/")
@RequestMapping("/payroll")
public class PayrollController {

    @Autowired
    private PayrollService payrollService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<?>> getPayroll(@RequestParam(required = true) Long employeeNum,
            @RequestParam(required = true, name = "startDate") String startDateStr,
            @RequestParam(required = true, name = "endDate") String endDateStr) {

        try {
            Date startDate = Date.valueOf(startDateStr);
            Date endDate = Date.valueOf(endDateStr);
            return ResponseEntity.ok().body(ApiResponse.success("Fetched payroll successfully.",
                    payrollService.generatePayrollForPeriod(employeeNum, startDate, endDate)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(Response.SC_BAD_REQUEST, "Failed to fetch payroll.",
                            Map.of("error", e.getMessage())));
        }
    }

    @GetMapping("/months")
    public ResponseEntity<ApiResponse<?>> getPayrollDates(@RequestParam(required = true) Integer year) {
        try {
            return ResponseEntity.ok().body(ApiResponse.success("Fetched payroll months successfully.",
                    payrollService.getPayrollStartDatesForYear(year)));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(ApiResponse.error(Response.SC_BAD_REQUEST, "Failed to fetch payroll months.",
                            Map.of("error", e.getMessage())));
        }
    }

}
