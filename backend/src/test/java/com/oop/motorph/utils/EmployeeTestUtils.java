package com.oop.motorph.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.oop.motorph.dto.EmployeeDTO;
import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.dto.UserDTO;
import com.oop.motorph.dto.mapper.EmployeeDTOMapper;
import com.oop.motorph.dto.mapper.EmployeeRequestDTOMapper;
import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.GovernmentIds;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.repository.EmployeeRepository;

@Component
public class EmployeeTestUtils {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmployeeDTOMapper employeeDTOMapper;

    @Autowired
    private EmployeeRequestDTOMapper employeeRequestDTOMapper;

    public String newFirstName = "Fultz";
    public String newLastName = "Vale";
    public String newStatus = "Probationary";
    public Double newRiceSubsidy = 2000D;
    public String newSSS = "123333";
    public String newUsername = "MarkelFultz";

    public UserDTO createUserDTO() {
        return new UserDTO("Morfuse");
    }

    public PersonalInfo createPersonalInfo() {
        return new PersonalInfo("Vale", "Markel", "1980-02-26", "lol 123 mactan ave", "0902082222");
    }

    public EmploymentInfo createEmploymentInfo() {
        return new EmploymentInfo("Regular", "Admin", "Boss Jam");
    }

    public GovernmentIds createGovernmentIds() {
        return new GovernmentIds("123123123", "123123123", "123123123", "123123123");
    }

    public Compensation createCompensation() {
        return new Compensation(100_000D, 1000D, 500D, 1000D, 50_000D, 1000D);
    }

    public Employee createAndSaveEmployee() {
        UserDTO user = createUserDTO();
        PersonalInfo personalInfo = createPersonalInfo();
        EmploymentInfo employmentInfo = createEmploymentInfo();
        GovernmentIds governmentIds = createGovernmentIds();
        Compensation compensation = createCompensation();

        EmployeeRequestDTO employeeRequest = new EmployeeRequestDTO(10001L, user, personalInfo, employmentInfo,
                governmentIds, compensation);

        Employee employee = employeeRequestDTOMapper.toEmployee(employeeRequest);
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Employee savedEmployee) {
        savedEmployee.getPersonalInfo().setFirstName(newFirstName);
        savedEmployee.getPersonalInfo().setLastName(newLastName);
        savedEmployee.getEmploymentInfo().setStatus(newStatus);
        savedEmployee.getCompensation().setRiceSubsidy(newRiceSubsidy);
        savedEmployee.getGovernmentIds().setSss(newSSS);
        savedEmployee.setUsername(newUsername);

        return employeeRepository.save(savedEmployee);
    }

    public EmployeeDTO deleteEmployee(Long deletedEmployeeNum) {
        // Find employee by employee number
        EmployeeDTO employeeDTO = employeeRepository.findByEmployeeNumber(deletedEmployeeNum)
                .map(employeeDTOMapper).orElse(null);

        // Delete employee using user id
        employeeRepository.deleteById(employeeDTO.employee().getUserId());

        // Try checking if the user still exists in the database
        return employeeRepository.findByEmployeeNumber(deletedEmployeeNum)
                .map(employeeDTOMapper).orElse(null);
    }

}
