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

/**
 * Represents an Employee entity in the MotorPH system, extending the
 * {@link User} entity.
 * This entity stores comprehensive employee details, including personal
 * information,
 * employment information, government IDs, and compensation, all mapped to the
 * "EMPLOYEE" table.
 * It uses a joined inheritance strategy with the User table, where the primary
 * key of Employee
 * is also a foreign key to the User table.
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "EMPLOYEE")
@EqualsAndHashCode(callSuper = true) // Ensures equals and hashCode consider superclass fields.
@PrimaryKeyJoinColumn(name = "userid") // Specifies the shared primary key column with the User entity.
@JsonIgnoreProperties(ignoreUnknown = true) // Ignores unknown JSON properties during deserialization.
// @Inheritance(strategy = InheritanceType.JOINED) // This line is commented out
// but indicates intended inheritance strategy.
public class Employee extends User {

    /**
     * The unique employee number.
     * This column is marked as unique and non-insertable/non-updatable directly
     * from the entity,
     * suggesting it's managed by the database (e.g., auto-generated or handled by
     * triggers).
     */
    @Column(name = "employeenum", unique = true, insertable = false, updatable = false)
    private Long employeeNumber;

    /**
     * Embedded object representing the employee's personal information.
     * The fields of {@link PersonalInfo} are mapped directly into the EMPLOYEE
     * table.
     */
    @Embedded
    private PersonalInfo personalInfo;

    /**
     * Embedded object representing the employee's employment information.
     * The fields of {@link EmploymentInfo} are mapped directly into the EMPLOYEE
     * table.
     */
    @Embedded
    private EmploymentInfo employmentInfo;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "governmentidentificationid", referencedColumnName = "id")
    private GovernmentIds governmentIds;

    /**
     * One-to-one relationship with {@link Compensation}.
     * {@code CascadeType.ALL} ensures that operations on the Employee entity are
     * cascaded
     * to the associated Compensation entity.
     * {@code @JoinColumn} specifies the foreign key column in the EMPLOYEE table
     * that references
     * the primary key of the Compensation table.
     */
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "compensationid", referencedColumnName = "id")
    private Compensation compensation;
}