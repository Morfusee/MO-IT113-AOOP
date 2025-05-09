package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// @NoArgsConstructor
// @AllArgsConstructor
// @Data
@Embeddable
public class EmploymentInfo {

    @Column(name = "status")
    private String status;

    @Column(name = "position")
    private String position;

    @Column(name = "immediate_supervisor")
    private String immediateSupervisor;

    // Constructors
    public EmploymentInfo() {
        this.status = "Regular";
        this.position = "N/A";
        this.immediateSupervisor = "N/A";
    }

    public EmploymentInfo(String status, String position, String immediateSupervisor) {
        this.status = status;
        this.position = position;
        this.immediateSupervisor = immediateSupervisor;
    }

    // Getters
    public String getStatus() {
        return status;
    }

    public String getPosition() {
        return position;
    }

    public String getImmediateSupervisor() {
        return immediateSupervisor;
    }

    // Setters
    public void setStatus(String status) {
        this.status = status;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setImmediateSupervisor(String immediateSupervisor) {
        this.immediateSupervisor = immediateSupervisor;
    }
}
