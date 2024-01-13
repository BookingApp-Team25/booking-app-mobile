package com.example.bookingapp.dto;

public class AccountEditRequest {
    private String password;
    private String passwordRepeat;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String address;

    public AccountEditRequest() {
    }

    public AccountEditRequest(String password, String passwordRepeat, String firstName, String lastName, String phoneNumber, String address) {
        this.password = password;
        this.passwordRepeat = passwordRepeat;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public String getPassword() {
        return password;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public void setPassword(String password) {
        this.password = password;
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

