package com.example.bookingapp.dto;

import com.example.bookingapp.dto.enums.AccommodationUpdateType;

import java.util.UUID;

public class AccommodationUpdateSummaryResponse {
    private UUID id;
    private String accommodationName;
    private String accommodationDescription;
    private String accommodationPhoto;
    private AccommodationUpdateType type;

    public AccommodationUpdateSummaryResponse() {
    }

    public AccommodationUpdateSummaryResponse(UUID id, String accommodationName, String accommodationDescription, String accommodationPhoto, AccommodationUpdateType type) {
        this.id = id;
        this.accommodationName = accommodationName;
        this.accommodationDescription = accommodationDescription;
        this.accommodationPhoto = accommodationPhoto;
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getAccommodationName() {
        return accommodationName;
    }

    public String getAccommodationDescription() {
        return accommodationDescription;
    }

    public String getAccommodationPhoto() {
        return accommodationPhoto;
    }

    public AccommodationUpdateType getType() {
        return type;
    }

    public void setAccommodationName(String accommodationName) {
        this.accommodationName = accommodationName;
    }

    public void setAccommodationDescription(String accommodationDescription) {
        this.accommodationDescription = accommodationDescription;
    }

    public void setAccommodationPhoto(String accommodationPhoto) {
        this.accommodationPhoto = accommodationPhoto;
    }

    public void setType(AccommodationUpdateType type) {
        this.type = type;
    }
}
