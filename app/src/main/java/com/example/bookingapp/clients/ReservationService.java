package com.example.bookingapp.clients;

import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReservationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ReservationService {
    @POST("reservation/create")
    Call<MessageResponse> createReservation(@Body ReservationRequest reservationRequest,  @Header("Authorization") String authorizationHeader);
}
