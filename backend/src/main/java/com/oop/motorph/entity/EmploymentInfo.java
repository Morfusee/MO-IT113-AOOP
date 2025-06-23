package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents embedded employment information for an employee.
 * This class is designed to be embedded within an {@link Employee} entity,
 * allowing its fields to be directly mapped into the same table as the owning
 * entity.
 * It provides default values for new instances.
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class EmploymentInfo {

    @Column(name = "status")
    private String status = "Regular"; // Default status for a new employee.

    @Column(name = "position")
    private String position = "N/A"; // Default position for a new employee.

    @Column(name = "immediate_supervisor")
    private String immediateSupervisor = "N/A"; // Default immediate supervisor for a new employee.
}