package com.example.bookingapp.dto;

import com.example.bookingapp.dto.enums.ReviewType;

public class ReviewRequest {
    String guestUsername;
    String reviewedEntity;
    String comment;
    double rating;
    ReviewType type;

    public ReviewRequest() {
    }

    public ReviewRequest(String comment, double rating, String guestUsername, String reviewedEntity, ReviewType type) {
        this.comment = comment;
        this.rating = rating;
        this.guestUsername = guestUsername;
        this.reviewedEntity = reviewedEntity;
        this.type = type;
    }

    public String getComment() {
        return comment;
    }

    public double getRating() {
        return rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getGuestUsername() {
        return guestUsername;
    }

    public String getReviewedEntity() {
        return reviewedEntity;
    }

    public ReviewType getType() {
        return type;
    }
}
