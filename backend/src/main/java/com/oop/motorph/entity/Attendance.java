package com.oop.motorph.entity;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ATTENDANCE")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Attendance {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @Column(name = "employeenum")
    private Long employeeNumber;

    @Column(name = "date")
    private Date date;

    @Column(name = "timeIn")
    private Time timeIn;

    @Column(name = "timeOut")
    private Time timeOut;

    @Transient
    private String status;

    @Transient
    private double totalHours;

    @Transient
    private double overtimeHours;

    // Constructors
    // DO NOT REMOVE THIS
    public Attendance(
            Long employeeNumber,
            Date date,
            Time timeIn,
            Time timeOut) {
        this.employeeNumber = employeeNumber;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        this.status = getStatus();
        this.totalHours = calculateTotalHours();
        this.overtimeHours = calculateOvertime();
    }

    // DO NOT REMOVE THIS
    public String getStatus() {
        if (calculateTotalHours() == 0) {
            return "Absent";
        }

        LocalTime inTime = timeIn.toLocalTime();
        LocalTime expectedInTime = LocalTime.of(8, 0); // Expected time in is 8:00 AM

        if (inTime.isAfter(expectedInTime)) {
            return "Late";
        }

        return "Present";
    }

    // Methods
    public double calculateTotalHours() {
        if (timeIn == null || timeOut == null) {
            return 0;
        }

        // Convert SQL Time to LocalTime for accurate duration calculation
        LocalTime inTime = timeIn.toLocalTime();
        LocalTime outTime = timeOut.toLocalTime();

        return roundToTwoDecimals((double) java.time.Duration.between(inTime, outTime).toMinutes() / 60);
    }

    public double calculateOvertime() {
        if (timeIn == null || timeOut == null) {
            return 0;
        }

        LocalTime outTime = timeOut.toLocalTime();
        LocalTime overtimeStart = LocalTime.of(17, 0); // Overtime starts after 5:00 PM

        if (outTime.isBefore(overtimeStart)) {
            return 0;
        }

        return roundToTwoDecimals((double) java.time.Duration.between(overtimeStart, outTime).toMinutes() / 60);
    }

    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100) / 100.0;
    }

}