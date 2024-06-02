package com.example.bookingapp.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.bookingapp.dto.enums.AccommodationOnHoldStatus;

import java.util.UUID;

public class AccommodationSummaryResponse implements Parcelable {
    private UUID accommodationId;
    private String name;
    private String photo;
    private String description;
    private double price;
    private double rating;
    private AccommodationOnHoldStatus onHoldStatus;

    public AccommodationSummaryResponse() {
    }

    public AccommodationSummaryResponse(UUID accommodationId, String name, String photo, String description, double price, double rating, AccommodationOnHoldStatus onHoldStatus) {
        this.accommodationId = accommodationId;
        this.name = name;
        this.photo = photo;
        this.description = description;
        this.price = price;
        this.rating = rating;
        this.onHoldStatus = onHoldStatus;
    }

    protected AccommodationSummaryResponse(Parcel in) {
        accommodationId = UUID.fromString(in.readString());
        name = in.readString();
        photo = in.readString();
        description = in.readString();
        price = in.readDouble();
        rating = in.readDouble();
        onHoldStatus = AccommodationOnHoldStatus.valueOf(in.readString());
    }

    public static final Creator<AccommodationSummaryResponse> CREATOR = new Creator<AccommodationSummaryResponse>() {
        @Override
        public AccommodationSummaryResponse createFromParcel(Parcel in) {
            return new AccommodationSummaryResponse(in);
        }

        @Override
        public AccommodationSummaryResponse[] newArray(int size) {
            return new AccommodationSummaryResponse[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accommodationId.toString());
        dest.writeString(name);
        dest.writeString(photo);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeDouble(rating);
        dest.writeString(onHoldStatus.name());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and setters
    // ...

    public UUID getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(UUID accommodationId) {
        this.accommodationId = accommodationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public AccommodationOnHoldStatus getOnHoldStatus() {
        return onHoldStatus;
    }

    public void setOnHoldStatus(AccommodationOnHoldStatus onHoldStatus) {
        this.onHoldStatus = onHoldStatus;
    }
}
