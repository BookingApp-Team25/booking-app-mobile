package com.example.bookingapp.clients;

import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.dto.ReservationSummaryCollectionResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReservationService {
    @POST("reservation/create")
    Call<MessageResponse> createReservation(@Body ReservationRequest reservationRequest,  @Header("Authorization") String authorizationHeader);

    @GET("accommodation/guest/{guestId}")
    Call<ReservationSummaryCollectionResponse> getGuestReservations(
            @Path("guestId") String guestId,
            @Query("page") int page,
            @Query("numberOfElements") int numberOfElements,
            @Header("Authorization") String authorizationHeader
    );

    @PUT("reservation/{reservationId}/delete")
    Call<MessageResponse> deleteReservation(
            @Path("reservationId") String reservationId,
            @Header("Authorization") String authorizationHeader
    );
}
