package com.oop.motorph.dto;

import com.oop.motorph.entity.Compensation;
import com.oop.motorph.entity.EmploymentInfo;
import com.oop.motorph.entity.GovernmentIds;
import com.oop.motorph.entity.PersonalInfo;

public record EmployeeRequestDTO(
        Long employeeNumber,
        UserDTO user,
        PersonalInfo personalInfo,
        EmploymentInfo employmentInfo,
        GovernmentIds governmentIds,
        Compensation compensation) {

}
