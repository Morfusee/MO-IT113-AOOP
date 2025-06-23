package com.oop.motorph.entity;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;
import java.time.Duration; // Explicitly import Duration

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

/**
 * Represents an attendance record for an employee.
 * This entity maps to the "ATTENDANCE" table in the database and includes
 * calculated transient fields for attendance status, total hours worked, and
 * overtime hours.
 */
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

    @Transient // Marks this field as not persistent in the database.
    private String status;

    @Transient // Marks this field as not persistent in the database.
    private double totalHours;

    @Transient // Marks this field as not persistent in the database.
    private double overtimeHours;

    /**
     * Custom constructor for creating an Attendance object.
     * This constructor initializes the core attendance fields and
     * immediately calculates and sets the transient status, total hours,
     * and overtime hours based on the provided time-in and time-out.
     *
     * @param employeeNumber The employee's unique identifier.
     * @param date           The date of the attendance.
     * @param timeIn         The recorded time-in.
     * @param timeOut        The recorded time-out.
     */
    public Attendance(
            Long employeeNumber,
            Date date,
            Time timeIn,
            Time timeOut) {
        this.employeeNumber = employeeNumber;
        this.date = date;
        this.timeIn = timeIn;
        this.timeOut = timeOut;
        // The status, totalHours, and overtimeHours are derived values.
        // Calling their respective getters/calculators here ensures they are set
        // immediately upon object creation using this constructor.
        this.status = getStatus();
        this.totalHours = calculateTotalHours();
        this.overtimeHours = calculateOvertime();
    }

    /**
     * Determines the attendance status based on calculated total hours and time-in.
     * - "Absent" if no hours are calculated.
     * - "Late" if time-in is after the expected 8:00 AM.
     * - "Present" otherwise.
     *
     * @return A string representing the attendance status.
     */
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

    /**
     * Calculates the total hours worked based on time-in and time-out.
     * Returns 0 if either time-in or time-out is null.
     * The result is rounded to two decimal places.
     *
     * @return The total hours worked, or 0 if times are not recorded.
     */
    public double calculateTotalHours() {
        if (timeIn == null || timeOut == null) {
            return 0;
        }

        LocalTime inTime = timeIn.toLocalTime();
        LocalTime outTime = timeOut.toLocalTime();

        // Calculate duration in minutes and convert to hours.
        return roundToTwoDecimals((double) Duration.between(inTime, outTime).toMinutes() / 60);
    }

    /**
     * Calculates the overtime hours based on time-out.
     * Overtime starts after 5:00 PM. Returns 0 if time-out is before the overtime
     * start.
     * Returns 0 if either time-in or time-out is null.
     * The result is rounded to two decimal places.
     *
     * @return The overtime hours, or 0 if no overtime was worked or times are not
     *         recorded.
     */
    public double calculateOvertime() {
        if (timeIn == null || timeOut == null) {
            return 0;
        }

        LocalTime outTime = timeOut.toLocalTime();
        LocalTime overtimeStart = LocalTime.of(17, 0); // Overtime starts after 5:00 PM

        if (outTime.isBefore(overtimeStart)) {
            return 0;
        }

        // Calculate duration in minutes from overtime start and convert to hours.
        return roundToTwoDecimals((double) Duration.between(overtimeStart, outTime).toMinutes() / 60);
    }

    /**
     * Rounds a double value to two decimal places.
     *
     * @param value The double value to round.
     * @return The rounded double value.
     */
    private double roundToTwoDecimals(double value) {
        return Math.round(value * 100) / 100.0;
    }
}