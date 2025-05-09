package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
// @Data
// @NoArgsConstructor
// @AllArgsConstructor
@Table(name = "COMPENSATION")

public class Compensation {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long compensationId;

    @Column(name = "basic_salary")
    private Double basicSalary;

    @Column(name = "rice_subsidy")
    private Double riceSubsidy;

    @Column(name = "phone_allowance")
    private Double phoneAllowance;

    @Column(name = "clothing_allowance")
    private Double clothingAllowance;

    @Column(name = "gross_semi_monthly_rate")
    private Double grossSemiMonthlyRate;

    @Column(name = "hourly_rate")
    private Double hourlyRate;

    // Constructors
    public Compensation() {
        this.basicSalary = 0.0;
        this.riceSubsidy = 0.0;
        this.phoneAllowance = 0.0;
        this.clothingAllowance = 0.0;
        this.grossSemiMonthlyRate = 0.0;
        this.hourlyRate = 0.0;
    }

    public Compensation(Double basicSalary, Double riceSubsidy, Double phoneAllowance, Double clothingAllowance,
            Double grossSemiMonthlyRate, Double hourlyRate) {
        this.basicSalary = basicSalary;
        this.riceSubsidy = riceSubsidy;
        this.phoneAllowance = phoneAllowance;
        this.clothingAllowance = clothingAllowance;
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
        this.hourlyRate = hourlyRate;
    }

    // Getters
    public Long getCompensationId() {
        return compensationId;
    }

    public Double getBasicSalary() {
        return basicSalary;
    }

    public Double getRiceSubsidy() {
        return riceSubsidy;
    }

    public Double getPhoneAllowance() {
        return phoneAllowance;
    }

    public Double getClothingAllowance() {
        return clothingAllowance;
    }

    public Double getGrossSemiMonthlyRate() {
        return grossSemiMonthlyRate;
    }

    public Double getHourlyRate() {
        return hourlyRate;
    }

    // Setters
    public void setCompensationId(Long compensationId) {
        this.compensationId = compensationId;
    }

    public void setBasicSalary(Double basicSalary) {
        this.basicSalary = basicSalary;
    }

    public void setRiceSubsidy(Double riceSubsidy) {
        this.riceSubsidy = riceSubsidy;
    }

    public void setPhoneAllowance(Double phoneAllowance) {
        this.phoneAllowance = phoneAllowance;
    }

    public void setClothingAllowance(Double clothingAllowance) {
        this.clothingAllowance = clothingAllowance;
    }

    public void setGrossSemiMonthlyRate(Double grossSemiMonthlyRate) {
        this.grossSemiMonthlyRate = grossSemiMonthlyRate;
    }

    public void setHourlyRate(Double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }
}
