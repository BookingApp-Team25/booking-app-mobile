package com.example.bookingapp.clients;

import com.example.bookingapp.dto.AccommodationRequest;
import com.example.bookingapp.dto.AccommodationUpdateSummaryResponse;
import com.example.bookingapp.dto.MessageResponse;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationUpdateService {
    @POST("accommodation-request")
    Call<MessageResponse> createAccommodationRequest(@Body AccommodationRequest accommodationRequest, @Header("Authorization") String authorizationHeader);
    @POST("accommodation-request/{accommodationId}")
    Call<MessageResponse> editAccommodationRequest(@Path("accommodationId")UUID id, @Body AccommodationRequest accommodationRequest, @Header("Authorization") String authorizationHeader);
    @GET("accommodation-request")
    Call<Collection<AccommodationUpdateSummaryResponse>> getAllUpdates(@Header("Authorization") String authorizationHeader);
    @PUT("accommodation-request/{accommodation-update-id}")
    Call<MessageResponse> resolveAccommodationUpdate(@Path("accommodation-update-id") UUID id, @Query("flag") int flag, @Header("Authorization") String authorizationHeader);
}
