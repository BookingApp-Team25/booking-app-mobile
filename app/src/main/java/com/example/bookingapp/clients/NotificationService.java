package com.example.bookingapp.clients;

import com.example.bookingapp.dto.AccommodationUpdateSummaryResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationRequest;
import com.example.bookingapp.dto.NotificationResponse;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface NotificationService {

    @GET("notification/{username}")
    Call<Collection<NotificationResponse>> getAllNotifications(@Path("username") String username, @Header("Authorization") String authorizationHeader);
    @POST("notification/send")
    Call<MessageResponse> sendNotification(@Body NotificationRequest notificationRequest, @Header("Authorization") String authorizationHeader);
    @PUT("notification/seen/{notificationId}")
    Call<MessageResponse> seenNotification(@Path("notificationId")UUID notificationId, @Header("Authorization") String authorizationHeader);
}
