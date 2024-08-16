package com.example.bookingapp.dto;

import java.time.LocalDateTime;

public class NotificationRequest {
    String receiver;
    String information;
    LocalDateTime sendTime;
    Boolean seen;

    public NotificationRequest() {

    }

    public NotificationRequest(String receiver, String information, LocalDateTime sendTime,Boolean seen) {
        this.receiver = receiver;
        this.information = information;
        this.sendTime = sendTime;
        this.seen = seen;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getInformation() {
        return information;
    }

    public LocalDateTime getSendTime() {
        return sendTime;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setInformation(String information) {
        this.information = information;
    }

    public void setSendTime(LocalDateTime sendTime) {
        this.sendTime = sendTime;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }
}
