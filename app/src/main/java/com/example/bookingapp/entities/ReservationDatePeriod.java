package com.example.bookingapp.entities;

import java.time.LocalDate;

public class ReservationDatePeriod {

    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isWeekendApplied;
    private boolean isSummerApplied;
    private boolean isWinterApplied;

    public ReservationDatePeriod(){

    }
    public ReservationDatePeriod(LocalDate startDate,LocalDate endDate){
        this.startDate = startDate;
        this.endDate = endDate;
        this.isWinterApplied = false;
        this.isSummerApplied = false;
        this.isWinterApplied = false;
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

    public boolean isWeekendApplied() {
        return isWeekendApplied;
    }

    public void setWeekendApplied(boolean weekendApplied) {
        isWeekendApplied = weekendApplied;
    }

    public boolean isSummerApplied() {
        return isSummerApplied;
    }

    public void setSummerApplied(boolean summerApplied) {
        isSummerApplied = summerApplied;
    }

    public boolean isWinterApplied() {
        return isWinterApplied;
    }

    public void setWinterApplied(boolean winterApplied) {
        isWinterApplied = winterApplied;
    }
}
