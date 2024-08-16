package com.example.bookingapp.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationResponse {

    UUID id;
    String receiver;
    String information;
    LocalDateTime sendTime;
    Boolean seen;
    public NotificationResponse() {
    }

    public NotificationResponse(UUID id,String receiver, String information, LocalDateTime sendTime,Boolean seen) {
        this.id = id;
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
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
