package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Explicitly import NoArgsConstructor

/**
 * Represents embedded personal information for an employee.
 * This class is designed to be embedded within an {@link Employee} entity,
 * allowing its fields to be directly mapped into the same table as the owning
 * entity.
 * It provides a default constructor that initializes all fields with "N/A".
 */
@AllArgsConstructor
@Data
@Embeddable
public class PersonalInfo {
    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "birthday")
    private String birthday;

    @Column(name = "address")
    private String address;

    @Column(name = "phone_number")
    private String phoneNumber;

    /**
     * Default constructor for PersonalInfo.
     * Initializes all string fields to "N/A". This is a common practice
     * when creating new entities to ensure fields are not null and have
     * a meaningful default state.
     */
    public PersonalInfo() {
        this.lastName = "N/A";
        this.firstName = "N/A";
        this.birthday = "N/A";
        this.address = "N/A";
        this.phoneNumber = "N/A";
    }
}