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
@Table(name = "GOVERNMENTIDENTIFICATION")

public class GovernmentIds {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long governmentId;

    @Column(name = "philhealth")
    private String philhealth = "N/A";

    @Column(name = "sss")
    private String sss = "N/A";

    @Column(name = "pagibig")
    private String pagibig = "N/A";

    @Column(name = "tin")
    private String tin = "N/A";

    public GovernmentIds(String philhealth, String sss, String pagibig, String tin) {
        this.philhealth = philhealth;
        this.sss = sss;
        this.pagibig = pagibig;
        this.tin = tin;
    }
}
