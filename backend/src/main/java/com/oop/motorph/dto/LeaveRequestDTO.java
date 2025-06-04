package com.oop.motorph.dto;

import java.sql.Timestamp;

import lombok.Builder;

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
