package com.example.bookingapp.entities;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

public class DatePeriod implements Parcelable {
    UUID id;
    private LocalDate startDate;
    private LocalDate endDate;

    public DatePeriod() {}

    public DatePeriod(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    protected DatePeriod(Parcel in) {
        id = UUID.fromString(in.readString());
        startDate = (LocalDate) in.readSerializable();
        endDate = (LocalDate) in.readSerializable();
    }

    public static final Creator<DatePeriod> CREATOR = new Creator<DatePeriod>() {
        @Override
        public DatePeriod createFromParcel(Parcel in) {
            return new DatePeriod(in);
        }

        @Override
        public DatePeriod[] newArray(int size) {
            return new DatePeriod[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id.toString());
        dest.writeSerializable(startDate);
        dest.writeSerializable(endDate);
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
}
