package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.AttendanceDTO;
import com.oop.motorph.entity.Attendance;

/**
 * A Spring service that implements the {@link Function} interface to map
 * {@link Attendance} entities to {@link AttendanceDTO} data transfer objects.
 * This mapper is responsible for transforming the domain model into a format
 * suitable for API responses, including calculated fields like total hours and
 * overtime.
 */
@Service
public class AttendanceDTOMapper implements Function<Attendance, AttendanceDTO> {

    /**
     * Applies this function to the given {@code attendance} entity, transforming it
     * into an {@code AttendanceDTO}.
     * It extracts relevant fields and calculates derived properties such as total
     * hours and overtime
     * directly from the {@link Attendance} entity's methods.
     *
     * @param attendance The {@link Attendance} entity to be mapped. Must not be
     *                   null.
     * @return A new {@link AttendanceDTO} instance populated with data from the
     *         attendance entity.
     */
    @Override
    public AttendanceDTO apply(Attendance attendance) {
        return new AttendanceDTO(
                attendance.getEmployeeNumber(),
                attendance.getDate(),
                attendance.getTimeIn(),
                attendance.getTimeOut(),
                attendance.getStatus(),
                attendance.calculateTotalHours(),
                attendance.calculateOvertime());
    }
}