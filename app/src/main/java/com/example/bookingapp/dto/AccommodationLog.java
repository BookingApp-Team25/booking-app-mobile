package com.example.bookingapp.dto;

import java.util.UUID;

public class AccommodationLog {
    UUID accommodationId; //id treba jer kad se na apartman klikne moracu da za njega dobavim dodatno
    String accommodationName;
    String accommodationPhoto;
    int reservationNumber;
    double totalProfit;

    public AccommodationLog() {
    }

    public AccommodationLog(UUID accommodationId, String accommodationName, String accommodationPhoto, int reservationNumber, double totalProfit) {
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.accommodationPhoto = accommodationPhoto;
        this.reservationNumber = reservationNumber;
        this.totalProfit = totalProfit;
    }
    public void increaseTotalProfit(double profit){
        this.totalProfit += profit;
    }
    public void incrementNumberOfReservations(){
        this.reservationNumber++;
    }
    public UUID getAccommodationId() {
        return accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public String getAccommodationPhoto() {
        return accommodationPhoto;
    }

    public int getReservationNumber() {
        return reservationNumber;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public void setAccommodationId(UUID accommodationId) {
        this.accommodationId = accommodationId;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public void setAccommodationPhoto(String accommodationPhoto) {
        this.accommodationPhoto = accommodationPhoto;
    }

    public void setReservationNumber(int reservationNumber) {
        this.reservationNumber = reservationNumber;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }
}
