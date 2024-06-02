package com.example.bookingapp.network;

import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReservationRequest;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ReservationService {
    @POST("reservation/create")
    Call<MessageResponse> createReservation(@Body ReservationRequest reservationRequest);
}
