package com.oop.motorph.entity;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

/**
 * Represents a comprehensive set of attendance analytics for an employee or a
 * group of employees.
 * This class provides methods to calculate various attendance metrics from a
 * list of {@link Attendance} records.
 */
public class AttendanceAnalytics {
    private Long totalPresent;
    private Long totalAbsent;
    private Double totalRenderedHours;
    private Time averageCheckIn;
    private Time averageCheckOut;
    private double totalOvertime;
    private Long totalLates; // Added this field as it's calculated and used

    /**
     * Constructs an AttendanceAnalytics object with pre-calculated attendance
     * metrics.
     *
     * @param totalPresent       The total number of present days.
     * @param totalAbsent        The total number of absent days.
     * @param totalRenderedHours The sum of all hours rendered.
     * @param averageCheckIn     The average check-in time.
     * @param averageCheckOut    The average check-out time.
     * @param totalOvertime      The sum of all overtime hours.
     */
    public AttendanceAnalytics(Long totalPresent, Long totalAbsent, Double totalRenderedHours, Time averageCheckIn,
            Time averageCheckOut, double totalOvertime) {
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.totalRenderedHours = totalRenderedHours;
        this.averageCheckIn = averageCheckIn;
        this.averageCheckOut = averageCheckOut;
        this.totalOvertime = totalOvertime;
    }

    /**
     * Constructs an AttendanceAnalytics object by calculating all metrics from a
     * list of {@link Attendance} records.
     *
     * @param attendances The list of {@link Attendance} records to analyze.
     */
    public AttendanceAnalytics(List<Attendance> attendances) {
        this.totalPresent = calculateTotalPresent(attendances);
        this.totalAbsent = calculateTotalAbsent(attendances);
        this.totalRenderedHours = calculateTotalRenderedHours(attendances);
        this.averageCheckIn = calculateAverageCheckIn(attendances);
        this.averageCheckOut = calculateAverageCheckOut(attendances);
        this.totalOvertime = attendances.stream().mapToDouble(Attendance::calculateOvertime).sum();
        this.totalLates = calculateTotalLates(attendances); // Initialize totalLates here
    }

    /**
     * Constructs an empty AttendanceAnalytics object with all metrics initialized
     * to default values.
     */
    public AttendanceAnalytics() {
        this.totalPresent = 0L;
        this.totalAbsent = 0L;
        this.totalRenderedHours = 0.0;
        this.averageCheckIn = null;
        this.averageCheckOut = null;
        this.totalOvertime = 0.0;
        this.totalLates = 0L; // Initialize totalLates
    }

    // --- Getters and Setters ---

    public Long getTotalPresent() {
        return totalPresent;
    }

    public void setTotalPresent(Long totalPresent) {
        this.totalPresent = totalPresent;
    }

    public Long getTotalAbsent() {
        return totalAbsent;
    }

    public void setTotalAbsent(Long totalAbsent) {
        this.totalAbsent = totalAbsent;
    }

    public Double getTotalRenderedHours() {
        return totalRenderedHours;
    }

    public void setTotalRenderedHours(Double totalRenderedHours) {
        this.totalRenderedHours = totalRenderedHours;
    }

    public Time getAverageCheckIn() {
        return averageCheckIn;
    }

    public void setAverageCheckIn(Time averageCheckIn) {
        this.averageCheckIn = averageCheckIn;
    }

    public Time getAverageCheckOut() {
        return averageCheckOut;
    }

    public void setAverageCheckOut(Time averageCheckOut) {
        this.averageCheckOut = averageCheckOut;
    }

    public double getTotalOvertime() {
        return totalOvertime;
    }

    public void setTotalOvertime(double totalOvertime) {
        this.totalOvertime = totalOvertime;
    }

    public Long getTotalLates() {
        return totalLates;
    }

    public void setTotalLates(Long totalLates) {
        this.totalLates = totalLates;
    }

    // --- Calculation Methods ---

    /**
     * Calculates the total number of "Present" attendance records from a given
     * list.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The count of present attendances.
     */
    public Long calculateTotalPresent(List<Attendance> attendances) {
        return attendances.stream().filter(attendance -> "Present".equals(attendance.getStatus())).count();
    }

    /**
     * Calculates the total number of "Absent" attendance records from a given list.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The count of absent attendances.
     */
    public Long calculateTotalAbsent(List<Attendance> attendances) {
        return attendances.stream().filter(attendance -> "Absent".equals(attendance.getStatus())).count();
    }

    /**
     * Calculates the sum of total hours rendered from a given list of attendance
     * records.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The sum of total hours.
     */
    public Double calculateTotalRenderedHours(List<Attendance> attendances) {
        return attendances.stream().mapToDouble(Attendance::calculateTotalHours).sum();
    }

    /**
     * Calculates the average check-in time from a list of attendance records.
     * Only valid check-in times (not null, not "00:00:00", and after 08:00:00) are
     * considered.
     * The result is returned as a {@link java.sql.Time} object.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The average check-in time, or {@code null} if no valid check-ins are
     *         found.
     */
    public Time calculateAverageCheckIn(List<Attendance> attendances) {
        List<LocalTime> validCheckIns = attendances.stream()
                .map(Attendance::getTimeIn)
                .filter(time -> time != null && !Time.valueOf("00:00:00").equals(time)) // Remove null & "00:00:00"
                .map(Time::toLocalTime) // Convert to LocalTime
                .filter(localTime -> localTime.isAfter(LocalTime.of(7, 59))) // Exclude before 08:00:00
                .collect(Collectors.toList());

        OptionalDouble averageCheckInSeconds = validCheckIns.stream()
                .mapToInt(LocalTime::toSecondOfDay)
                .average();

        return averageCheckInSeconds.isPresent()
                ? Time.valueOf(LocalTime.ofSecondOfDay((long) averageCheckInSeconds.getAsDouble()))
                : null;
    }

    /**
     * Calculates the average check-out time from a list of attendance records.
     * Only valid check-out times (not null, not "00:00:00", and after 08:00:00) are
     * considered.
     * The result is returned as a {@link java.sql.Time} object.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The average check-out time, or {@code null} if no valid check-outs
     *         are found.
     */
    public Time calculateAverageCheckOut(List<Attendance> attendances) {
        List<LocalTime> validCheckOuts = attendances.stream()
                .map(Attendance::getTimeOut)
                .filter(time -> time != null && !Time.valueOf("00:00:00").equals(time)) // Remove null & "00:00:00"
                .map(Time::toLocalTime) // Convert to LocalTime
                .filter(localTime -> localTime.isAfter(LocalTime.of(8, 0))) // Exclude before 08:00:00
                .collect(Collectors.toList());

        OptionalDouble averageCheckOutSeconds = validCheckOuts.stream()
                .mapToInt(LocalTime::toSecondOfDay)
                .average();

        return averageCheckOutSeconds.isPresent()
                ? Time.valueOf(LocalTime.ofSecondOfDay((long) averageCheckOutSeconds.getAsDouble()))
                : null;
    }

    /**
     * Calculates the total number of "Late" attendance records from a given list.
     *
     * @param attendances A list of {@link Attendance} records.
     * @return The count of late attendances.
     */
    public Long calculateTotalLates(List<Attendance> attendances) {
        return attendances.stream().filter(attendance -> "Late".equals(attendance.getStatus())).count();
    }
}