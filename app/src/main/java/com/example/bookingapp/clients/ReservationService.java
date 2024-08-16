package com.example.bookingapp.clients;

import androidx.annotation.Nullable;

import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.HostReservationCollectionResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReservationRequest;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {
    @POST("reservation/create")
    Call<MessageResponse> createReservation(@Body ReservationRequest reservationRequest,@Header("Authorization") String authorizationHeader);
    @GET("reservation/{hostId}/results")
    Call<HostReservationCollectionResponse> getHostReservations(@Path("hostId") UUID hostId,@Query("page") @Nullable int page, @Query("numberOfElements") @Nullable int numberOfElements,@Header("Authorization") String authorizationHeader);
    @GET("reservation/{hostId}/unresolved")
    Call<HostReservationCollectionResponse> getUnresolvedHostReservations(@Path("hostId") UUID hostId,@Query("page") @Nullable int page, @Query("numberOfElements") @Nullable int numberOfElements,@Header("Authorization") String authorizationHeader);
    @POST("reservation/{reservationId}/resolve")
    Call<MessageResponse> resolveReservationRequest(@Path("reservationId") UUID reservationId, @Query("isAccepted") boolean isAccepted, @Header("Authorization") String authorizationHeader);
}
