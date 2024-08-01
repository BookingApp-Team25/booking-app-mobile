package com.example.bookingapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class DatePeriod {
    UUID id;
    private LocalDate startDate;
    private LocalDate endDate;

    public DatePeriod() {}

    public DatePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public long getDuration() {
        return ChronoUnit.DAYS.between(startDate, endDate);
    }

    public long calculateDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }

    @Override
    public String toString() {
        return "DatePeriod{" +
                "id=" + id +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
