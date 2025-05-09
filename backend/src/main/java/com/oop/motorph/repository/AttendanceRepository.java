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

    List<Attendance> findByEmployeeNumber(Long employeeNumber);

    List<Attendance> findByEmployeeNumberAndDateBetween(Long employeeNumber, Date startDate,
            Date endDate);

    // @Query("SELECT DISTINCT CAST(FORMATDATETIME(a.date, 'yyyy-MM-01') AS date) AS
    // uniqueMonth " +
    // "FROM Attendance a ORDER BY uniqueMonth")
    // List<Date> findPayrollDates();

    @Query("SELECT DISTINCT CAST(FORMATDATETIME(a.date, 'yyyy-MM-01') AS date) AS uniqueMonth " +
            "FROM Attendance a " +
            "WHERE YEAR(a.date) = :year " +
            "ORDER BY uniqueMonth")
    List<Date> findPayrollDatesByYear(@Param("year") int year);
}
