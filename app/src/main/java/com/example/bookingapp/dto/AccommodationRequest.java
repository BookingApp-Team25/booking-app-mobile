package com.example.bookingapp.dto;

import com.example.bookingapp.dto.enums.AccommodationReservationPolicy;
import com.example.bookingapp.dto.enums.AccommodationType;
import com.example.bookingapp.dto.enums.PriceCalculationMethod;
import com.example.bookingapp.entities.AccommodationPricelist;
import com.example.bookingapp.entities.Location;
import com.example.bookingapp.entities.ReservationDatePeriod;

import java.util.List;
import java.util.UUID;

public class AccommodationRequest {
    private String hostId;
    private String name;
    private String description;
    private Location location;
    private List<String> amenities;
    private List<String>  photos;
    private int minGuests;
    private int maxGuests;
    private AccommodationType type;
    private List<ReservationDatePeriod> availability;
    private AccommodationPricelist pricelist;
    private double price;
    private int daysBefore;
    private AccommodationReservationPolicy policy;
    private PriceCalculationMethod priceCalculationMethod;

    public AccommodationRequest() {
    }

    public AccommodationRequest(String hostId, String name, String description, Location location, List<String> amenities, List<String> photos, int minGuests, int maxGuests, AccommodationType type, double price, List<ReservationDatePeriod> datePeriods , AccommodationPricelist pricelist, int daysBefore, AccommodationReservationPolicy policy, PriceCalculationMethod priceCalculationMethod) {
        this.hostId = hostId;
        this.name = name;
        this.description = description;
        this.location = location;
        this.amenities = amenities;
        this.minGuests = minGuests;
        this.maxGuests = maxGuests;
        this.type = type;
        this.photos = photos;
        this.availability = datePeriods;
        this.price = price;
        this.pricelist = pricelist;
        this.daysBefore = daysBefore;
        this.policy = policy;
        this.priceCalculationMethod = priceCalculationMethod;
    }

    public String getHostId() {
        return hostId;
    }

    public void setHostId(String hostId) {
        this.hostId = hostId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getAmenities() {
        return amenities;
    }

    public List<String> getPhotos() {
        return photos;
    }

    public int getMinGuests() {
        return minGuests;
    }

    public int getMaxGuests() {
        return maxGuests;
    }

    public AccommodationType getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public void setAmenities(List<String> amenities) {
        this.amenities = amenities;
    }

    public void setPhotos(List<String> photos) {
        this.photos = photos;
    }

    public void setMinGuests(int minGuests) {
        this.minGuests = minGuests;
    }

    public void setMaxGuests(int maxGuests) {
        this.maxGuests = maxGuests;
    }

    public void setType(AccommodationType type) {
        this.type = type;
    }

    public void setAvailability(List<ReservationDatePeriod> availability) {
        this.availability = availability;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ReservationDatePeriod> getAvailability() {
        return availability;
    }

    public AccommodationPricelist getPricelist() {
        return pricelist;
    }

    public void setPricelist(AccommodationPricelist pricelist) {
        this.pricelist = pricelist;
    }

    public int getDaysBefore() {
        return daysBefore;
    }

    public AccommodationReservationPolicy getPolicy() {
        return policy;
    }

    public void setDaysBefore(int daysBefore) {
        this.daysBefore = daysBefore;
    }

    public void setPolicy(AccommodationReservationPolicy policy) {
        this.policy = policy;
    }

    public PriceCalculationMethod getPriceCalculationMethod() {
        return priceCalculationMethod;
    }

    public void setPriceCalculationMethod(PriceCalculationMethod priceCalculationMethod) {
        this.priceCalculationMethod = priceCalculationMethod;
    }
}
