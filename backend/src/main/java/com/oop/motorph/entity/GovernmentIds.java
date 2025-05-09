package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
@Table(name = "GOVERNMENTIDENTIFICATION")

public class GovernmentIds {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long governmentId;

    @Column(name = "philhealth")
    private String philhealth;

    @Column(name = "sss")
    private String sss;

    @Column(name = "pagibig")
    private String pagibig;

    @Column(name = "tin")
    private String tin;

    // Constructors
    public GovernmentIds() {
        this.philhealth = "N/A";
        this.sss = "N/A";
        this.pagibig = "N/A";
        this.tin = "N/A";
    }

    public GovernmentIds(String philhealth, String sss, String pagibig, String tin) {
        this.philhealth = philhealth;
        this.sss = sss;
        this.pagibig = pagibig;
        this.tin = tin;
    }

    // Getters
    public Long getGovernmentId() {
        return governmentId;
    }

    public String getPhilhealth() {
        return philhealth;
    }

    public String getSss() {
        return sss;
    }

    public String getPagibig() {
        return pagibig;
    }

    public String getTin() {
        return tin;
    }

    // Setters
    public void setGovernmentId(Long governmentId) {
        this.governmentId = governmentId;
    }

    public void setPhilhealth(String philhealth) {
        this.philhealth = philhealth;
    }

    public void setSss(String sss) {
        this.sss = sss;
    }

    public void setPagibig(String pagibig) {
        this.pagibig = pagibig;
    }

    public void setTin(String tin) {
        this.tin = tin;
    }
}
