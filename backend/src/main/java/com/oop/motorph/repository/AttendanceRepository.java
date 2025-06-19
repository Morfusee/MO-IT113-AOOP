package com.oop.motorph.repository;

import java.sql.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.oop.motorph.entity.Attendance;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

        /**
         * Retrieves all attendance records for a given employee.
         * 
         * @param employeeNumber The unique employee number.
         * @return List of Attendance records.
         */
        List<Attendance> findByEmployeeNumber(Long employeeNumber);

        /**
         * Retrieves attendance records for a specific employee within a date range.
         * 
         * @param employeeNumber The unique employee number.
         * @param startDate      The start date of the range.
         * @param endDate        The end date of the range.
         * @return List of Attendance records in the given date range.
         */
        List<Attendance> findByEmployeeNumberAndDateBetween(Long employeeNumber, Date startDate, Date endDate);

        /**
         * Retrieves the distinct months (formatted as the first day of the month)
         * from attendance records for a specific year.
         * 
         * This is used to determine available payroll months.
         * 
         * @param year The year to filter attendance records.
         * @return List of distinct first-day-of-month dates.
         */
        @Query("SELECT DISTINCT CAST(FORMATDATETIME(a.date, 'yyyy-MM-01') AS date) AS uniqueMonth " +
                        "FROM Attendance a " +
                        "WHERE YEAR(a.date) = :year " +
                        "ORDER BY uniqueMonth")
        List<Date> findPayrollDatesByYear(@Param("year") int year);
}
