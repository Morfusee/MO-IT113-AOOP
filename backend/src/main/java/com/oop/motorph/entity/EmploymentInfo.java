package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Embeddable
public class EmploymentInfo {

    @Column(name = "status")
    private String status = "Regular";

    @Column(name = "position")
    private String position = "N/A";

    @Column(name = "immediate_supervisor")
    private String immediateSupervisor = "N/A";
}
