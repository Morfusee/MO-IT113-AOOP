package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;

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

    // Constructors
    public PersonalInfo() {
        this.lastName = "N/A";
        this.firstName = "N/A";
        this.birthday = "N/A";
        this.address = "N/A";
        this.phoneNumber = "N/A";
    }
}
