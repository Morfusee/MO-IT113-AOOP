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

@Entity
// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
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

    // Constructors
    public LeaveRequest() {
        this.employeeNum = 0L;
        this.startDate = new Timestamp(System.currentTimeMillis());
        this.endDate = new Timestamp(System.currentTimeMillis());
        this.notes = "N/A";
        this.leaveType = "N/A";
        this.status = "N/A";
    }

    public LeaveRequest(Long employeeNum, Timestamp startDate, Timestamp endDate, String notes, String leaveType,
            String status) {
        this.employeeNum = employeeNum;
        this.startDate = startDate;
        this.endDate = endDate;
        this.notes = notes;
        this.leaveType = leaveType;
        this.status = status;
    }

    // Getters
    public Integer getId() {
        return id;
    }

    public Long getEmployeeNum() {
        return employeeNum;
    }

    public Employee getEmployee() {
        return employee;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public String getNotes() {
        return notes;
    }

    public String getLeaveType() {
        return leaveType;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(Integer id) {
        this.id = id;
    }

    public void setEmployeeNum(Long employeeNum) {
        this.employeeNum = employeeNum;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setLeaveType(String leaveType) {
        this.leaveType = leaveType;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
