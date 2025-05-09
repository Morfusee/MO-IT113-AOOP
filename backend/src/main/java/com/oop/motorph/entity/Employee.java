package com.oop.motorph.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;

@Entity
// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
@Table(name = "EMPLOYEE")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "userid")
@JsonIgnoreProperties(ignoreUnknown = true)

public class Employee extends User {

    // This solves auto incrementing issue for employee number
    @Column(name = "employeenum", unique = true, insertable = false, updatable = false)
    private Long employeeNumber;

    @Embedded
    private PersonalInfo personalInfo;

    @Embedded
    private EmploymentInfo employmentInfo;

    // Cascading them enables adding them in one go
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "governmentidentificationid", referencedColumnName = "id")
    private GovernmentIds governmentIds;

    // Cascading them enables adding them in one go
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "compensationid", referencedColumnName = "id")
    private Compensation compensation;

    // Constructors
    public Employee() {
        super();
    }

    public Employee(Long employeeNumber, PersonalInfo personalInfo, EmploymentInfo employmentInfo,
            GovernmentIds governmentIds, Compensation compensation) {
        super();
        this.employeeNumber = employeeNumber;
        this.personalInfo = personalInfo;
        this.employmentInfo = employmentInfo;
        this.governmentIds = governmentIds;
        this.compensation = compensation;
    }

    // Getters
    public Long getEmployeeNumber() {
        return employeeNumber;
    }

    public PersonalInfo getPersonalInfo() {
        return personalInfo;
    }

    public EmploymentInfo getEmploymentInfo() {
        return employmentInfo;
    }

    public GovernmentIds getGovernmentIds() {
        return governmentIds;
    }

    public Compensation getCompensation() {
        return compensation;
    }

    // Setters

    public void setEmployeeNumber(Long employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    public void setPersonalInfo(PersonalInfo personalInfo) {
        this.personalInfo = personalInfo;
    }

    public void setEmploymentInfo(EmploymentInfo employmentInfo) {
        this.employmentInfo = employmentInfo;
    }

    public void setGovernmentIds(GovernmentIds governmentIds) {
        this.governmentIds = governmentIds;
    }

    public void setCompensation(Compensation compensation) {
        this.compensation = compensation;
    }

}
