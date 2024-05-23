package com.example.bookingapp.network;

import com.example.bookingapp.Accommodation;
import com.example.bookingapp.dto.AccommodationSummaryResponse;

import java.util.Collection;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface AccommodationService {
    @GET("accommodation")
    Call<Collection<AccommodationSummaryResponse>> getAllAccommodations();
}
