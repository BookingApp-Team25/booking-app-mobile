package com.example.bookingapp.entities;

import java.util.UUID;

public class AccommodationPricelist {
    private UUID id;
    private double dailyPrice;
    private double weekendPrice;
    private double summerPrice;
    private double winterPrice;


    public AccommodationPricelist(double dailyPrice, double weekendPrice, double summerPrice, double winterPrice) {
        this.dailyPrice = dailyPrice;
        this.weekendPrice = weekendPrice;
        this.summerPrice = summerPrice;
        this.winterPrice = winterPrice;
    }

    public AccommodationPricelist() {
        this.dailyPrice = 0.0;
        this.weekendPrice = 0.0;
        this.summerPrice = 0.0;
        this.winterPrice = 0.0;
    }

    public double getDailyPrice() {
        return dailyPrice;
    }

    public double getWeekendPrice() {
        return weekendPrice;
    }

    public double getSummerPrice() {
        return summerPrice;
    }

    public double getWinterPrice() {
        return winterPrice;
    }

    public void setDailyPrice(double dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public void setWeekendPrice(double weekendPrice) {
        this.weekendPrice = weekendPrice;
    }

    public void setSummerPrice(double summerPrice) {
        this.summerPrice = summerPrice;
    }

    public void setWinterPrice(double winterPrice) {
        this.winterPrice = winterPrice;
    }
}
