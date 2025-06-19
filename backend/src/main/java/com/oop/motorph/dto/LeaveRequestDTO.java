package com.oop.motorph.dto;

import java.sql.Timestamp;

import lombok.Builder;

/**
 * A Data Transfer Object (DTO) for conveying leave request information.
 * This record class is used to expose leave request details, including employee
 * information,
 * leave period, type, and status, typically for API responses or display
 * purposes.
 * The {@code @Builder} annotation from Lombok automatically generates a builder
 * for this record, facilitating object creation.
 *
 * @param id           The unique identifier of the leave request.
 * @param employeeNum  The employee number associated with the leave request.
 * @param employeeName The full name of the employee requesting leave.
 * @param startDate    The start date and time of the leave period.
 * @param endDate      The end date and time of the leave period.
 * @param notes        Additional notes or reasons for the leave request.
 * @param leaveType    The type of leave (e.g., "Vacation Leave", "Sick Leave").
 * @param status       The current status of the leave request (e.g., "Pending",
 *                     "Approved", "Rejected").
 */
@Builder
public record LeaveRequestDTO(
        Integer id,
        Long employeeNum,
        String employeeName,
        Timestamp startDate,
        Timestamp endDate,
        String notes,
        String leaveType,
        String status) {
}