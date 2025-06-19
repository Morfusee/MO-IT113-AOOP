package com.oop.motorph.entity;

import java.sql.Timestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Represents a leave request submitted by an employee.
 * This entity maps to the "LEAVEREQUEST" table in the database.
 * It includes details such as the employee, leave period, type, and status.
 */
@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "LEAVEREQUEST")
public class LeaveRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "employeenum", nullable = false)
    private Long employeeNum;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employeenum", referencedColumnName = "employeenum", insertable = false, updatable = false)
    private Employee employee;

    @Column(name = "startdate", nullable = false)
    private Timestamp startDate;

    @Column(name = "enddate", nullable = false)
    private Timestamp endDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "leave_type", nullable = false)
    private String leaveType;

    @Column(name = "status", nullable = false)
    private String status;

    /**
     * Default constructor for LeaveRequest.
     * Initializes fields with default or current timestamp values.
     */
    public LeaveRequest() {
        this.employeeNum = 0L;
        this.startDate = new Timestamp(System.currentTimeMillis());
        this.endDate = new Timestamp(System.currentTimeMillis());
        this.notes = "N/A";
        this.leaveType = "N/A";
        this.status = "N/A";
    }

    /**
     * Parameterized constructor for LeaveRequest.
     * Initializes a LeaveRequest with specified details.
     *
     * @param employeeNum The employee number submitting the request.
     * @param startDate   The start date and time of the leave.
     * @param endDate     The end date and time of the leave.
     * @param notes       Any additional notes or reasons for the leave.
     * @param leaveType   The type of leave (e.g., sick, vacation).
     * @param status      The current status of the leave request (e.g., pending,
     *                    approved).
     */
    public LeaveRequest(Long employeeNum, Timestamp startDate, Timestamp endDate, String notes, String leaveType,
            String status) {
        this.employeeNum = employeeNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.leaveType = leaveType;
        this.status = status;
    }
}