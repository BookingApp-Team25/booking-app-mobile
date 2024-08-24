package com.example.bookingapp.dto;

import java.util.List;
import java.util.UUID;

public class AccommodationMonthlyLogCollection {
    UUID accommodationId;
    String accommodationName;
    List<AccommodationMonthlyLog> months;

    public AccommodationMonthlyLogCollection() {
    }

    public AccommodationMonthlyLogCollection(UUID accommodationId, String accommodationName, List<AccommodationMonthlyLog> months) {
        this.accommodationId = accommodationId;
        this.accommodationName = accommodationName;
        this.months = months;
    }

    public UUID getAccommodationId() {
        return accommodationId;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public List<AccommodationMonthlyLog> getMonths() {
        return months;
    }

    public void setAccommodationId(UUID accommodationId) {
        this.accommodationId = accommodationId;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public void setMonths(List<AccommodationMonthlyLog> months) {
        this.months = months;
    }
}
