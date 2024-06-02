package com.example.bookingapp.clients;

import com.example.bookingapp.dto.AccountDetailsResponse;
import com.example.bookingapp.dto.AccountEditRequest;
import com.example.bookingapp.dto.MessageResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface UserService {

    @GET("user/details/{username}")
    Call<AccountDetailsResponse> getAccountDetails(
            @Path("username") String username,
            @Header("Authorization") String authorizationHeader
    );

    @PUT("user/{username}")
    Call<MessageResponse> editAccount(@Path("username") String username, @Body AccountEditRequest accountEditRequest,
                                      @Header("Authorization") String authorizationHeader);

    @DELETE("user/{username}")
    Call<MessageResponse> deleteAccount(@Path("username") String username,@Header("Authorization") String authorizationHeader);

}
