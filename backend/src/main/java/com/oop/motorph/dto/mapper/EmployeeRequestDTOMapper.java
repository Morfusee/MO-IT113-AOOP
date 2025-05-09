package com.oop.motorph.dto.mapper;

import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.oop.motorph.dto.EmployeeRequestDTO;
import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.Employee;
import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.GovernmentIds;
import com.oop.motorph.entity.PersonalInfo;
import com.oop.motorph.utils.ApiResponse;

@Service
public class EmployeeRequestDTOMapper implements Function<Employee, EmployeeRequestDTO> {

    public Employee toEmployee(EmployeeRequestDTO employeeRequest) {
        // Validate fields
        fieldValidators(employeeRequest);

        // Create new employee
        Employee employee = new Employee();
        employee.setEmployeeNumber(employeeRequest.employeeNumber());
        employee.setUsername(employeeRequest.user().username());
        employee.setPassword("password");
        employee.setPersonalInfo(employeeRequest.personalInfo());
        employee.setEmploymentInfo(employeeRequest.employmentInfo());
        employee.setGovernmentIds(employeeRequest.governmentIds());
        employee.setCompensation(employeeRequest.compensation());

        return employee;
    }

    public GovernmentIds toGovernmentIds(EmployeeRequestDTO employeeRequest) {
        GovernmentIds governmentIds = new GovernmentIds();
        governmentIds.setPagibig(employeeRequest.governmentIds().getPagibig());
        governmentIds.setPhilhealth(employeeRequest.governmentIds().getPhilhealth());
        governmentIds.setSss(employeeRequest.governmentIds().getSss());
        governmentIds.setTin(employeeRequest.governmentIds().getTin());

        return governmentIds;
    }

    @Override
    public EmployeeRequestDTO apply(Employee employee) {
        return new EmployeeRequestDTO(null, null, null, null, null, null);
    }

    public void updateEntity(EmployeeRequestDTO employeeRequestDTO, Employee employee) {

        if (employeeRequestDTO == null || employee == null) {
            throw new IllegalArgumentException("employeeRequestDTO or employee is null");
        }

        if (employeeRequestDTO.employeeNumber() != null) {
            employee.setEmployeeNumber(employeeRequestDTO.employeeNumber());
        }

        if (employeeRequestDTO.user() != null && employeeRequestDTO.user().username() != null) {
            employee.setUsername(employeeRequestDTO.user().username());
        }

        if (employeeRequestDTO.personalInfo() != null) {
            updatePersonalInfo(employeeRequestDTO, employee);
        }

        if (employeeRequestDTO.employmentInfo() != null) {
            updateEmploymentInfo(employeeRequestDTO, employee);
        }

        if (employeeRequestDTO.governmentIds() != null) {
            updateGovernmentIds(employeeRequestDTO, employee);
        }

        if (employeeRequestDTO.compensation() != null) {
            updateCompensation(employeeRequestDTO, employee);
        }
    }

    public void updatePersonalInfo(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        PersonalInfo personalInfo = employeeRequestDTO.personalInfo();

        if (employee.getPersonalInfo() == null) {
            employee.setPersonalInfo(new PersonalInfo());
        }

        if (personalInfo.getAddress() != null) {
            employee.getPersonalInfo().setAddress(personalInfo.getAddress());
        }

        if (personalInfo.getBirthday() != null) {
            employee.getPersonalInfo().setBirthday(personalInfo.getBirthday());
        }

        if (personalInfo.getFirstName() != null) {
            employee.getPersonalInfo().setFirstName(personalInfo.getFirstName());
        }

        if (personalInfo.getLastName() != null) {
            employee.getPersonalInfo().setLastName(personalInfo.getLastName());
        }

        if (personalInfo.getPhoneNumber() != null) {
            employee.getPersonalInfo().setPhoneNumber(personalInfo.getPhoneNumber());
        }
    }

    public void updateEmploymentInfo(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        EmploymentInfo employmentInfo = employeeRequestDTO.employmentInfo();

        if (employee.getEmploymentInfo() == null) {
            employee.setEmploymentInfo(new EmploymentInfo());
        }

        if (employmentInfo.getImmediateSupervisor() != null) {
            employee.getEmploymentInfo()
                    .setImmediateSupervisor(employmentInfo.getImmediateSupervisor());
        }

        if (employmentInfo.getPosition() != null) {
            employee.getEmploymentInfo()
                    .setPosition(employmentInfo.getPosition());
        }

        if (employmentInfo.getStatus() != null) {
            employee.getEmploymentInfo()
                    .setStatus(employmentInfo.getStatus());
        }
    }

    public void updateGovernmentIds(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        GovernmentIds governmentIds = employeeRequestDTO.governmentIds();

        // If governmentIds is null, create a new one
        if (employee.getGovernmentIds() == null) {
            employee.setGovernmentIds(new GovernmentIds());
        }

        if (governmentIds.getPagibig() != null) {
            employee.getGovernmentIds().setPagibig(governmentIds.getPagibig());
        }

        if (governmentIds.getPhilhealth() != null) {
            employee.getGovernmentIds().setPhilhealth(governmentIds.getPhilhealth());
        }

        if (governmentIds.getSss() != null) {
            employee.getGovernmentIds().setSss(governmentIds.getSss());
        }

        if (governmentIds.getTin() != null) {
            employee.getGovernmentIds().setTin(governmentIds.getTin());
        }
    }

    public void updateCompensation(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        Compensation compensation = employeeRequestDTO.compensation();

        // If compensation is null, create a new one
        if (employee.getCompensation() == null) {
            employee.setCompensation(new Compensation());
        }

        if (compensation.getBasicSalary() != null) {
            employee.getCompensation().setBasicSalary(compensation.getBasicSalary());
        }

        if (compensation.getClothingAllowance() != null) {
            employee.getCompensation()
                    .setClothingAllowance(compensation.getClothingAllowance());
        }

        if (compensation.getGrossSemiMonthlyRate() != null) {
            employee.getCompensation()
                    .setGrossSemiMonthlyRate(compensation.getGrossSemiMonthlyRate());
        }

        if (compensation.getHourlyRate() != null) {
            employee.getCompensation().setHourlyRate(compensation.getHourlyRate());
        }

        if (compensation.getPhoneAllowance() != null) {
            employee.getCompensation()
                    .setPhoneAllowance(compensation.getPhoneAllowance());
        }

        if (compensation.getRiceSubsidy() != null) {
            employee.getCompensation().setRiceSubsidy(compensation.getRiceSubsidy());
        }
    }

    public void fieldValidators(EmployeeRequestDTO employeeRequestDTO) {
        if (employeeRequestDTO.user() == null) {
            throw new RuntimeException(ApiResponse.badRequestException("Username is required").getMessage());
        }

        if (employeeRequestDTO.personalInfo() == null) {
            throw new RuntimeException(ApiResponse.badRequestException("Personal Info is required").getMessage());
        }

        if (employeeRequestDTO.employmentInfo() == null) {
            throw new RuntimeException(ApiResponse.badRequestException("Employment Info is required").getMessage());
        }
    }
}
