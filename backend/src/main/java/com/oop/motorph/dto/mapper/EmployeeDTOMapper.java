package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.entity.Employee;

@Service
public class EmployeeDTOMapper implements Function<Employee, EmployeeDTO> {
    @Override
    public EmployeeDTO apply(Employee employee) {
        return new EmployeeDTO(employee);
    }

}
