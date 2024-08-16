package com.example.bookingapp.clients;

import androidx.annotation.Nullable;

import com.example.bookingapp.dto.AccommodationSummaryCollectionResponse;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.AccommodationDetailsResponse;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    @GET("accommodation/host/{host_id}")
    Call<AccommodationSummaryCollectionResponse> getHostAccommodations(
            @Path("host_id") String hostId,
            @Query("page") @Nullable int page,
            @Query("numberOfElements") @Nullable int numberOfElements,
            @Header("Authorization") String authorizationHeader
    );
}
