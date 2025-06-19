package com.oop.motorph.dto;

import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.GovernmentIds;
import com.oop.motorph.entity.PersonalInfo;

/**
 * A Data Transfer Object (DTO) for handling employee creation or update
 * requests.
 * This record class aggregates various pieces of an employee's information,
 * including personal, employment, government ID, and compensation details,
 * along with user authentication data, into a single object for API
 * consumption.
 *
 * @param employeeNumber The unique employee number.
 * @param user           The {@link UserDTO} containing user authentication
 *                       details (e.g., username).
 * @param personalInfo   The {@link PersonalInfo} entity containing personal
 *                       details of the employee.
 * @param employmentInfo The {@link EmploymentInfo} entity containing
 *                       employment-related details.
 * @param governmentIds  The {@link GovernmentIds} entity containing government
 *                       identification numbers.
 * @param compensation   The {@link Compensation} entity containing salary and
 *                       allowance details.
 */
public record EmployeeRequestDTO(
                Long employeeNumber,
                UserDTO user,
                PersonalInfo personalInfo,
                EmploymentInfo employmentInfo,
                GovernmentIds governmentIds,
                Compensation compensation) {

}