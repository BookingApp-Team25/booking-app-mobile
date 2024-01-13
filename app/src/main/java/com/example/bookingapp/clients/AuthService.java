package com.example.bookingapp.clients;

import com.example.bookingapp.dto.LoginRequest;
import com.example.bookingapp.dto.LoginResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.RegistrationRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface AuthService {

    @POST("auth/register")
    Call<MessageResponse> register(@Body RegistrationRequest registrationRequest);

    @POST("auth/login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);

    @GET("auth/logout")
    Call<MessageResponse> logout(@Header("Authorization") String authorizationHeader);
}
