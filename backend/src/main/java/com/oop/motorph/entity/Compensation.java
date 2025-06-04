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
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
