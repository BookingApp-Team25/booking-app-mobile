package com.example.bookingapp.dto;

import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.entities.DatePeriod;

import java.util.UUID;

public class HostReservationResponse {
    private UUID reservationId;
    private String guestName;
    private UUID accommodationId;
    private String accommodationName;
    private String accommodationPhoto;
    private ReservationStatus reservationStatus;
    private DatePeriod reservedDate;

    double price;

    public HostReservationResponse() {
    }

    public HostReservationResponse(UUID reservationId, String guestName, UUID accommodationId, String accommodationName, String accommodationPhoto , ReservationStatus reservationStatus, DatePeriod reservedDate, double price) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.reservationStatus = reservationStatus;
        this.reservedDate = reservedDate;
        this.price = price;
        this.accommodationPhoto = accommodationPhoto;
    }

    public String getAccommodationPhoto() {
        return accommodationPhoto;
    }

    public void setAccommodationPhoto(String accommodationPhoto) {
        this.accommodationPhoto = accommodationPhoto;
    }

    public void setReservationId(UUID reservationId) {
        this.reservationId = reservationId;
    }

    public void setGuestName(String guestName) {
        this.guestName = guestName;
    }

    public void setAccommodationId(UUID accommodationId) {
        this.accommodationId = accommodationId;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void setReservedDate(DatePeriod reservedDate) {
        this.reservedDate = reservedDate;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public UUID getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public UUID getAccommodationId() {
        return accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public DatePeriod getReservedDate() {
        return reservedDate;
    }

    public double getPrice() {
        return price;
    }
}
