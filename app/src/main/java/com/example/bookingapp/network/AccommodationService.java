package com.example.bookingapp.network;

import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.AccommodationDetailsResponse;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface AccommodationService {
    @GET("accommodation")
    Call<Collection<AccommodationSummaryResponse>> getAllAccommodations();

    @POST("accommodation/details/{accommodationId}")
    Call<AccommodationDetailsResponse> getAccommodation(@Path("accommodationId") UUID accommodationId);
}
