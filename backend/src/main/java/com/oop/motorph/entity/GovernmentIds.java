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

/**
 * Represents government identification details for an employee.
 * This entity maps to the "GOVERNMENTIDENTIFICATION" table in the database.
 * It stores various government-issued IDs, with default values if not provided.
 */
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
    private String philhealth = "N/A"; // Default value for PhilHealth ID.

    @Column(name = "sss")
    private String sss = "N/A"; // Default value for SSS ID.

    @Column(name = "pagibig")
    private String pagibig = "N/A"; // Default value for Pag-IBIG ID.

    @Column(name = "tin")
    private String tin = "N/A"; // Default value for TIN.

    /**
     * Constructs a GovernmentIds object with specified government identification
     * numbers.
     *
     * @param philhealth The PhilHealth ID.
     * @param sss        The SSS ID.
     * @param pagibig    The Pag-IBIG ID.
     * @param tin        The TIN.
     */
    public GovernmentIds(String philhealth, String sss, String pagibig, String tin) {
        this.philhealth = philhealth;
        this.sss = sss;
        this.pagibig = pagibig;
        this.tin = tin;
    }
}