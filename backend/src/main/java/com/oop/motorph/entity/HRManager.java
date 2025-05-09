package com.oop.motorph.entity;

import lombok.EqualsAndHashCode;

// @Data
@EqualsAndHashCode(callSuper = true)
public class HRManager extends Employee {

    public HRManager() {
        super();
    }

    public HRManager(Long employeeNumber, PersonalInfo personalInfo, EmploymentInfo employmentInfo,
            GovernmentIds governmentIds, Compensation compensation) {
        super(employeeNumber, personalInfo, employmentInfo, governmentIds, compensation);
    }
}
