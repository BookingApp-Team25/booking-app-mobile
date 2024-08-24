package com.example.bookingapp.clients;

import androidx.annotation.Nullable;

import com.example.bookingapp.dto.AccommodationLogCollection;
import com.example.bookingapp.dto.AccommodationMonthlyLogCollection;
import com.example.bookingapp.dto.AccommodationSummaryCollectionResponse;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.ReservationSummaryCollectionResponse;

import java.util.Collection;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface AccommodationService {
    @GET("accommodation")
    Call<Collection<AccommodationSummaryResponse>> getAllAccommodations();

    @POST("accommodation/details/{accommodationId}")
    Call<AccommodationDetailsResponse> getAccommodation(@Path("accommodationId") UUID accommodationId,@Header("Authorization") String authorizationHeader);
    @GET("accommodation/approved")
    Call<AccommodationSummaryCollectionResponse> getAllApprovedAccommodations();

    @GET("accommodation/results")
    Call<Collection<AccommodationSummaryResponse>> searchAccommodations(
            @Query("city") String city,
            @Query("dateStart") String dateStart,
            @Query("dateEnd") String dateEnd,
            @Query("guestNumber") int guestNumber
    );
    @GET("accommodation/filtered")
    Call<Collection<AccommodationSummaryResponse>> searchAccommodationsFiltered(
            @Query("city") String city,
            @Query("dateStart") String dateStart,
            @Query("dateEnd") String dateEnd,
            @Query("guestNumber") int guestNumber,
            @Query("amenities") String amenities,
            @Query("accommodationType") String accommodationType,
            @Query("minPrice") Double minPrice,
            @Query("maxPrice") Double maxPrice
    );
    @GET("accommodation/get-favourite/{guestUsername}")
    Call<Collection<AccommodationSummaryResponse>> getFavouriteAccommodations(@Path("guestUsername") String guestUsername,@Header("Authorization") String authorizationHeader);
    @GET("accommodation/host/{host_id}")
    Call<AccommodationSummaryCollectionResponse> getHostAccommodations(
            @Path("host_id") String hostId,
            @Query("page") @Nullable int page,
            @Query("numberOfElements") @Nullable int numberOfElements,
            @Header("Authorization") String authorizationHeader
    );
    @GET("host/{hostUsername}/log")
    Call<AccommodationLogCollection> generateLogs(@Path("hostUsername") String hostUsername,@Query("startDateStr") String startDateStr, @Query("endDateStr") String endDateStr,@Header("Authorization") String authorizationHeader);
    @GET("host/{accommodationId}/annual-log")
    Call<AccommodationMonthlyLogCollection> generateAnnualLog(@Path("accommodationId") UUID accommodationId,@Header("Authorization") String authorizationHeader);
    @PUT("accommodation/add-favourite/{guestUsername}/{accommodationId}")
    Call<Boolean> addFavouriteAccommodation(@Path("guestUsername") String guestUsername,@Path("accommodationId") UUID accommodationId,@Header("Authorization") String authorizationHeader);
    @PUT("accommodation/remove-favourite/{guestUsername}/{accommodationId}")
    Call<Boolean> removeFavouriteAccommodation(@Path("guestUsername") String guestUsername, @Path("accommodationId") String accommodationId,@Header("Authorization") String authorizationHeader);
    @GET("accommodation/guest/{guestUsername}")
    Call<ReservationSummaryCollectionResponse> getGuestReservations(@Path("guestUsername") String guestUsername,@Header("Authorization") String authorizationHeader);
}

