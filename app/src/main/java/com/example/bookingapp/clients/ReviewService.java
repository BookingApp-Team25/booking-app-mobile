package com.example.bookingapp.clients;

import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReviewRequest;
import com.example.bookingapp.dto.ReviewResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ReviewService {

    @GET("review/{reviewedEntity}")
    Call<List<ReviewResponse>> getAllReviews(@Path("reviewedEntity") String reviewedEntity, @Query("flag") Boolean flag);

    @DELETE("review/{reviewId}")
    Call<Boolean> deleteReview(@Path("reviewId") String reviewId, @Query("flag") Boolean flag, @Header("Authorization") String authorizationHeader);

    @POST("review/report/{reviewId}")
    Call<MessageResponse> reportReview(@Path("reviewId") String reviewId, @Query("flag") Boolean flag, @Header("Authorization") String authorizationHeader);

    @POST("review")
    Call<MessageResponse> createReview(@Body ReviewRequest reviewRequest, @Header("Authorization") String authorizationHeader);


}
