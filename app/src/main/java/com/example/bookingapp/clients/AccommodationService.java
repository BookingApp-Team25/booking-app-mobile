package com.example.bookingapp.clients;

import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.AccommodationDetailsResponse;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {
    @GET("accommodation")
    Call<Collection<AccommodationSummaryResponse>> getAllAccommodations();

    @POST("accommodation/details/{accommodationId}")
    Call<AccommodationDetailsResponse> getAccommodation(@Path("accommodationId") UUID accommodationId);

    @GET("accommodation/results")
    Call<Collection<AccommodationSummaryResponse>> searchAccommodations(
            @Query("city") String city,
            @Query("dateStart") String startDate,
            @Query("dateEnd") String endDate,
            @Query("guestNumber") int guestNumber
    );
}
