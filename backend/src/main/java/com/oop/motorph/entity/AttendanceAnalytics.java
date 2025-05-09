package com.oop.motorph.entity;

import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.OptionalDouble;
import java.util.stream.Collectors;

public class AttendanceAnalytics {
    private Long totalPresent;
    private Long totalAbsent;
    private Double totalRenderedHours;
    private Time averageCheckIn;
    private Time averageCheckOut;
    private double totalOvertime;

    // Constructors
    public AttendanceAnalytics(Long totalPresent, Long totalAbsent, Double totalRenderedHours, Time averageCheckIn,
            Time averageCheckOut, double totalOvertime) {
        this.totalPresent = totalPresent;
        this.totalAbsent = totalAbsent;
        this.totalRenderedHours = totalRenderedHours;
        this.averageCheckIn = averageCheckIn;
        this.averageCheckOut = averageCheckOut;
        this.totalOvertime = totalOvertime;
    }

    public AttendanceAnalytics(List<Attendance> attendances) {
        this.totalPresent = calculateTotalPresent(attendances);
        this.totalAbsent = calculateTotalAbsent(attendances);
        this.totalRenderedHours = calculateTotalRenderedHours(attendances);
        this.averageCheckIn = calculateAverageCheckIn(attendances);
        this.averageCheckOut = calculateAverageCheckOut(attendances);
        this.totalOvertime = attendances.stream().mapToDouble(Attendance::calculateOvertime).sum();
    }

    public AttendanceAnalytics() {
        this.totalPresent = 0L;
        this.totalAbsent = 0L;
        this.totalRenderedHours = 0.0;
        this.averageCheckIn = null;
        this.averageCheckOut = null;
        this.totalOvertime = 0.0;
    }

    // Getters and Setters
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

    // Methods
    public Long calculateTotalPresent(List<Attendance> attendances) {
        return attendances.stream().filter(attendance -> attendance.getStatus().equals("Present")).count();
    }

    public Long calculateTotalAbsent(List<Attendance> attendances) {
        return attendances.stream().filter(attendance -> attendance.getStatus().equals("Absent")).count();
    }

    public Double calculateTotalRenderedHours(List<Attendance> attendances) {
        return attendances.stream().mapToDouble(Attendance::calculateTotalHours).sum();
    }

    public Time calculateAverageCheckIn(List<Attendance> attendances) {
        List<LocalTime> validCheckIns = attendances.stream()
                .map(Attendance::getTimeIn)
                .filter(time -> time != null && !time.equals(Time.valueOf("00:00:00"))) // Remove null & "00:00:00"
                .map(Time::toLocalTime) // Convert to LocalTime
                .filter(localTime -> localTime.isAfter(LocalTime.of(7, 59))) // Exclude before 08:00:00
                .collect(Collectors.toList());

        OptionalDouble averageCheckIn = validCheckIns.stream()
                .mapToInt(LocalTime::toSecondOfDay)
                .average();

        return averageCheckIn.isPresent() ? Time.valueOf(LocalTime.ofSecondOfDay((long) averageCheckIn.getAsDouble()))
                : null;
    }

    public Time calculateAverageCheckOut(List<Attendance> attendances) {
        List<LocalTime> validCheckOuts = attendances.stream()
                .map(Attendance::getTimeOut)
                .filter(time -> time != null && !time.equals(Time.valueOf("00:00:00"))) // Remove null & "00:00:00"
                .map(Time::toLocalTime) // Convert to LocalTime
                .filter(localTime -> localTime.isAfter(LocalTime.of(8, 0))) // Exclude before 08:00:00
                .collect(Collectors.toList());

        OptionalDouble averageCheckOut = validCheckOuts.stream()
                .mapToInt(LocalTime::toSecondOfDay)
                .average();

        return averageCheckOut.isPresent() ? Time.valueOf(LocalTime.ofSecondOfDay((long) averageCheckOut.getAsDouble()))
                : null;
    }

    public Long calculateTotalLates(List<Attendance> attendances) {
        return attendances.stream().filter(attendance -> attendance.getStatus().equals("Late")).count();
    }
}
