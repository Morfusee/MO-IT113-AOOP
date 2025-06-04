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
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
@EqualsAndHashCode(callSuper = true)
@PrimaryKeyJoinColumn(name = "userid")
@JsonIgnoreProperties(ignoreUnknown = true)
// @Inheritance(strategy = InheritanceType.JOINED)

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
}
