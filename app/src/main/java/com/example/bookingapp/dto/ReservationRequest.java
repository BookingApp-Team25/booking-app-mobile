package com.example.bookingapp.dto;

import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.entities.DatePeriod;

import java.util.UUID;

public class ReservationRequest {
    private UUID reservationId;

    private UUID guestId;

    private UUID hostId;

    private UUID accommodationId;

    private ReservationStatus reservationStatus;

    private DatePeriod reservedDate;

    private long price;

    public ReservationRequest() {
    }

    public ReservationRequest(UUID reservationId , UUID guestId, UUID hostId, UUID accommodationId, ReservationStatus reservationStatus, DatePeriod reservedDate, long price) {
        this.reservationId = reservationId;
        this.guestId = guestId;
        this.hostId = hostId;
        this.accommodationId = accommodationId;
        this.reservationStatus = reservationStatus;
        this.reservedDate = reservedDate;
        this.price = price;
    }

    public UUID getReservationId() { return reservationId; }

    public void setReservationId(UUID reservationId) { this.reservationId = reservationId; }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public UUID getGuestId() {
        return guestId;
    }

    public UUID getHostId() {
        return hostId;
    }

    public UUID getAccommodationId() {
        return accommodationId;
    }

    public ReservationStatus getReservationStatus() {
        return reservationStatus;
    }

    public DatePeriod getReservedDate() {
        return reservedDate;
    }

    public void setReservationStatus(ReservationStatus reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public void setReservedDate(DatePeriod reservedDate) {
        this.reservedDate = reservedDate;
    }

    public void setGuestId(UUID guestId) {
        this.guestId = guestId;
    }

    public void setHostId(UUID hostId) {
        this.hostId = hostId;
    }

    public void setAccommodationId(UUID accommodationId) {
        this.accommodationId = accommodationId;
    }

    @Override
    public String toString() {
        return "ReservationRequest{" +
                "reservationId=" + reservationId +
                ", guestId=" + guestId +
                ", hostId=" + hostId +
                ", accommodationId=" + accommodationId +
                ", reservationStatus=" + reservationStatus +
                ", reservedDate=" + reservedDate.toString() +
                ", price=" + price +
                '}';
    }
}
