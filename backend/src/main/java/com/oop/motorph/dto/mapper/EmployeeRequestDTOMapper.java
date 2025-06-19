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

/**
 * Maps between {@link Employee} entities and {@link EmployeeRequestDTO}
 * objects.
 * This mapper handles the conversion of incoming DTOs to entities for
 * persistence
 * and updates existing entities based on DTO content. It also performs basic
 * validation.
 */
@Service
public class EmployeeRequestDTOMapper implements Function<Employee, EmployeeRequestDTO> {

    /**
     * Converts an {@code EmployeeRequestDTO} to an {@code Employee} entity.
     * This method initializes a new Employee entity and populates its fields
     * from the DTO, including nested information like personal, employment,
     * government ID, and compensation details. It also sets a default password.
     *
     * @param employeeRequest The {@link EmployeeRequestDTO} to convert.
     * @return A new {@link Employee} entity.
     * @throws RuntimeException if required fields in the DTO are missing.
     */
    public Employee toEmployee(EmployeeRequestDTO employeeRequest) {
        // Validate required fields before creating the employee entity.
        fieldValidators(employeeRequest);

        Employee employee = new Employee();
        employee.setEmployeeNumber(employeeRequest.employeeNumber());
        employee.setUsername(employeeRequest.user().username());
        employee.setPassword("password"); // Default password set during creation.
        employee.setPersonalInfo(employeeRequest.personalInfo());
        employee.setEmploymentInfo(employeeRequest.employmentInfo());
        employee.setGovernmentIds(employeeRequest.governmentIds());
        employee.setCompensation(employeeRequest.compensation());

        return employee;
    }

    /**
     * Converts an {@code EmployeeRequestDTO} to a {@code GovernmentIds} entity.
     * This method extracts and maps only the government ID information
     * from the DTO to a new GovernmentIds entity.
     *
     * @param employeeRequest The {@link EmployeeRequestDTO} containing government
     *                        ID details.
     * @return A new {@link GovernmentIds} entity.
     */
    public GovernmentIds toGovernmentIds(EmployeeRequestDTO employeeRequest) {
        GovernmentIds governmentIds = new GovernmentIds();
        governmentIds.setPagibig(employeeRequest.governmentIds().getPagibig());
        governmentIds.setPhilhealth(employeeRequest.governmentIds().getPhilhealth());
        governmentIds.setSss(employeeRequest.governmentIds().getSss());
        governmentIds.setTin(employeeRequest.governmentIds().getTin());

        return governmentIds;
    }

    /**
     * This method is part of the {@link Function} interface implementation,
     * but its current implementation returns a new {@link EmployeeRequestDTO}
     * with null values, indicating it might not be fully implemented or
     * intended for a different mapping direction (Employee to EmployeeRequestDTO).
     *
     * @param employee The {@link Employee} entity to convert.
     * @return A new {@link EmployeeRequestDTO} with null fields.
     */
    @Override
    public EmployeeRequestDTO apply(Employee employee) {
        return new EmployeeRequestDTO(null, null, null, null, null, null);
    }

    /**
     * Updates an existing {@link Employee} entity with data from an
     * {@link EmployeeRequestDTO}.
     * This method performs a partial update, only applying non-null fields from the
     * DTO
     * to the corresponding fields in the entity, including nested informational
     * objects.
     *
     * @param employeeRequestDTO The {@link EmployeeRequestDTO} containing updated
     *                           data.
     * @param employee           The existing {@link Employee} entity to update.
     * @throws IllegalArgumentException if either the DTO or the entity is null.
     */
    public void updateEntity(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        if (employeeRequestDTO == null || employee == null) {
            throw new IllegalArgumentException("employeeRequestDTO or employee cannot be null.");
        }

        if (employeeRequestDTO.employeeNumber() != null) {
            employee.setEmployeeNumber(employeeRequestDTO.employeeNumber());
        }

        if (employeeRequestDTO.user() != null && employeeRequestDTO.user().username() != null) {
            employee.setUsername(employeeRequestDTO.user().username());
        }

        // Delegate updates for nested objects to specific helper methods.
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

    /**
     * Updates the {@link PersonalInfo} of an {@link Employee} entity using data
     * from an {@link EmployeeRequestDTO}.
     * If the employee's personal info is null, a new {@link PersonalInfo} object is
     * created.
     * Individual fields are updated only if they are not null in the DTO.
     *
     * @param employeeRequestDTO The {@link EmployeeRequestDTO} with personal
     *                           information updates.
     * @param employee           The {@link Employee} entity whose personal info is
     *                           to be updated.
     */
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

    /**
     * Updates the {@link EmploymentInfo} of an {@link Employee} entity using data
     * from an {@link EmployeeRequestDTO}.
     * If the employee's employment info is null, a new {@link EmploymentInfo}
     * object is created.
     * Individual fields are updated only if they are not null in the DTO.
     *
     * @param employeeRequestDTO The {@link EmployeeRequestDTO} with employment
     *                           information updates.
     * @param employee           The {@link Employee} entity whose employment info
     *                           is to be updated.
     */
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

    /**
     * Updates the {@link GovernmentIds} of an {@link Employee} entity using data
     * from an {@link EmployeeRequestDTO}.
     * If the employee's government IDs are null, a new {@link GovernmentIds} object
     * is created.
     * Individual fields are updated only if they are not null in the DTO.
     *
     * @param employeeRequestDTO The {@link EmployeeRequestDTO} with government ID
     *                           updates.
     * @param employee           The {@link Employee} entity whose government IDs
     *                           are to be updated.
     */
    public void updateGovernmentIds(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        GovernmentIds governmentIds = employeeRequestDTO.governmentIds();

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

    /**
     * Updates the {@link Compensation} details of an {@link Employee} entity using
     * data from an {@link EmployeeRequestDTO}.
     * If the employee's compensation is null, a new {@link Compensation} object is
     * created.
     * Individual fields are updated only if they are not null in the DTO.
     *
     * @param employeeRequestDTO The {@link EmployeeRequestDTO} with compensation
     *                           updates.
     * @param employee           The {@link Employee} entity whose compensation is
     *                           to be updated.
     */
    public void updateCompensation(EmployeeRequestDTO employeeRequestDTO, Employee employee) {
        Compensation compensation = employeeRequestDTO.compensation();

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

    /**
     * Validates essential fields in an {@link EmployeeRequestDTO}.
     * This method ensures that critical information, such as user details, personal
     * info,
     * and employment info, are present in the DTO before further processing.
     *
     * @param employeeRequestDTO The {@link EmployeeRequestDTO} to validate.
     * @throws RuntimeException if any required field is missing, wrapping an
     *                          {@link ApiResponse#badRequestException(String)}.
     */
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