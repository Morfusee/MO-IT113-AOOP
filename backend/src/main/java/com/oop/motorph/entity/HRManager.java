package com.oop.motorph.entity;

import lombok.EqualsAndHashCode;
import lombok.Data;

/**
 * Represents an HR Manager, extending the {@link Employee} entity.
 * This class inherits all properties and behaviors of an Employee,
 * with no additional fields defined, indicating it serves primarily
 * as a role-based distinction or for future specialized HR functionalities.
 */
@Data
@EqualsAndHashCode(callSuper = true) // Ensures that equals and hashCode methods consider fields from the Employee
                                     // superclass.
public class HRManager extends Employee {

    /**
     * Default constructor for HRManager.
     * Calls the default constructor of the {@link Employee} superclass.
     */
    public HRManager() {
        super();
    }

    /**
     * Parameterized constructor for HRManager.
     * Initializes an HRManager object with common employee details by
     * calling the corresponding constructor of the {@link Employee} superclass.
     *
     * @param employeeNumber The unique employee number of the HR Manager.
     * @param personalInfo   The personal information of the HR Manager.
     * @param employmentInfo The employment information of the HR Manager.
     * @param governmentIds  The government identification details of the HR
     *                       Manager.
     * @param compensation   The compensation details of the HR Manager.
     */
    public HRManager(Long employeeNumber, PersonalInfo personalInfo, EmploymentInfo employmentInfo,
            GovernmentIds governmentIds, Compensation compensation) {
        super(employeeNumber, personalInfo, employmentInfo, governmentIds, compensation);
    }
}