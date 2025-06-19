package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.Employee;

/**
 * Maps {@link Employee} entities to {@link EmployeeDTO} objects.
 */
@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {
    /**
     * Converts an {@code Employee} entity to an {@code EmployeeDTO}.
     *
     * @param employee The {@link Employee} entity to map.
     * @return A new {@link EmployeeDTO} containing the employee data.
     */
    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(employee);
    }

}