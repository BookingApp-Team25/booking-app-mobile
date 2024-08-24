package com.example.bookingapp.dto;

public class AccommodationMonthlyLog {
    String monthName;
    double totalProfit;
    int numberOfReservations;

    public AccommodationMonthlyLog() {
    }

    public AccommodationMonthlyLog(String monthName, double totalProfit, int numberOfReservations) {
        this.monthName = monthName;
        this.totalProfit = totalProfit;
        this.numberOfReservations = numberOfReservations;
    }

    public String getMonthName() {
        return monthName;
    }

    public double getTotalProfit() {
        return totalProfit;
    }

    public int getNumberOfReservations() {
        return numberOfReservations;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public void setTotalProfit(double totalProfit) {
        this.totalProfit = totalProfit;
    }

    public void setNumberOfReservations(int numberOfReservations) {
        this.numberOfReservations = numberOfReservations;
    }
}
