package com.example.bookingapp.entities;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class DatePeriod {
    UUID id;
    private LocalDate startDate;
    private LocalDate endDate;

    public DatePeriod() {
    }

    public DatePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
    public long getDuration(){
        return ChronoUnit.DAYS.between(startDate,endDate);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    public long calculateDurationInDays() {
        return ChronoUnit.DAYS.between(startDate, endDate) + 1;
    }
}
