package com.example.bookingapp.dto;

import java.util.UUID;

public class UserReportResponse {
    UUID userId;
    String username;
    String reason;
    String role;

    public UserReportResponse() {}
    public UserReportResponse(UUID userId, String reason,String username, String role) {
        this.userId = userId;
        this.reason = reason;
        this.username = username;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID id) {
        this.userId = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
