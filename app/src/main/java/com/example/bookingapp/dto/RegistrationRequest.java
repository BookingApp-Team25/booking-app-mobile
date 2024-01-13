package com.example.bookingapp.dto;

import com.example.bookingapp.dto.enums.Role;

public class RegistrationRequest {
    private String username;
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;
    private Role role;

    public RegistrationRequest() {
    }

    public RegistrationRequest(String username, String password, String passwordRepeat, String firstName, String lastName, String phoneNumber, String address, Role role) {
        this.username = username;
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
