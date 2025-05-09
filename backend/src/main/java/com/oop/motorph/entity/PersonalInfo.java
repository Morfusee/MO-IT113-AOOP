package com.oop.motorph.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

// @NoArgsConstructor
// @AllArgsConstructor
// @Data
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

    public PersonalInfo(String lastName, String firstName, String birthday, String address, String phoneNumber) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.phoneNumber = phoneNumber;
    }

    // Getters
    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    // Setters
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
