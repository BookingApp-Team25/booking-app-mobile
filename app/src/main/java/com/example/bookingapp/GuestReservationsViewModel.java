package com.example.bookingapp;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookingapp.activities.ReservationActivity;
import com.example.bookingapp.clients.AccommodationService;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.clients.ReservationService;
import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.dto.ReservationSummaryCollectionResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestReservationsViewModel extends ViewModel {
    private final MutableLiveData<List<ReservationRequest>> reservations;
    private final MutableLiveData<Map<String, String>> accommodationNames;
    private final ReservationService reservationService;
    private final AccommodationService accommodationService;
    private final MutableLiveData<String> deletionStatus;

    public GuestReservationsViewModel() {
        reservations = new MutableLiveData<>();
        accommodationNames = new MutableLiveData<>(new HashMap<>());
        deletionStatus = new MutableLiveData<>();
        reservationService = ClientUtils.reservationService;
        accommodationService = ClientUtils.accommodationService;
    }

    public LiveData<String> getDeletionStatus() {
        return deletionStatus;
    }

    public LiveData<List<ReservationRequest>> getReservations() {
        return reservations;
    }

    public LiveData<Map<String, String>> getAccommodationNames() {
        return accommodationNames;
    }

    public void fetchReservations(String guestId, int page, int numberOfElements, String authorizationHeader) {
        reservationService.getGuestReservations(guestId, page, numberOfElements, authorizationHeader).enqueue(new Callback<ReservationSummaryCollectionResponse>() {
            @Override
            public void onResponse(Call<ReservationSummaryCollectionResponse> call, Response<ReservationSummaryCollectionResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<ReservationRequest> reservationList = new ArrayList<>(response.body().getSummaries());
                    reservations.setValue(reservationList);
                    fetchAccommodationNames(reservationList, authorizationHeader);
                } else {
                    Log.e("GuestReservationsVM", "Failed to fetch reservations: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ReservationSummaryCollectionResponse> call, Throwable t) {
                Log.e("GuestReservationsVM", "Failed to fetch reservations: " + t.getMessage());
            }
        });
    }

    private void fetchAccommodationNames(List<ReservationRequest> reservationList, String authorizationHeader) {
        Map<String, String> namesMap = new HashMap<>();
        final int[] pendingRequests = {reservationList.size()}; // Track pending requests

        for (ReservationRequest reservation : reservationList) {
            accommodationService.getAccommodation(reservation.getAccommodationId()).enqueue(new Callback<AccommodationDetailsResponse>() {
                @Override
                public void onResponse(Call<AccommodationDetailsResponse> call, Response<AccommodationDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        namesMap.put(reservation.getAccommodationId().toString(), response.body().getName());
                        accommodationNames.postValue(new HashMap<>(namesMap)); // Notify observers with new map instance

                        pendingRequests[0]--;
                        if (pendingRequests[0] == 0) {
                            Log.d("GuestReservationsVM", "All accommodation names fetched");
                            // Notify any additional observers or UI elements if needed
                        }
                    } else {
                        Log.e("GuestReservationsVM", "Failed to fetch accommodation name: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<AccommodationDetailsResponse> call, Throwable t) {
                    Log.e("GuestReservationsVM", "Failed to fetch accommodation name: " + t.getMessage());
                    pendingRequests[0]--;
                    if (pendingRequests[0] == 0) {
                        Log.d("GuestReservationsVM", "All accommodation names fetched with errors");
                        // Notify any additional observers or UI elements if needed
                    }
                }
            });
        }
    }

    public void deleteReservation(String reservationId, String authorizationHeader) {
        reservationService.deleteReservation(reservationId, authorizationHeader).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    List<ReservationRequest> currentReservations = reservations.getValue();
                    if (currentReservations != null) {
                        currentReservations.removeIf(reservation -> reservation.getReservationId().equals(reservationId));
                        reservations.setValue(currentReservations);
                    }
                    deletionStatus.setValue("Reservation deleted successfully");
                    Log.d("GuestReservationsVM", "Reservation deleted successfully");
                } else {
                    deletionStatus.setValue("Failed to delete reservation: " + response.message());
                    Log.e("GuestReservationsVM", "Failed to delete reservation: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                deletionStatus.setValue("Failed to delete reservation: " + t.getMessage());
                Log.e("GuestReservationsVM", "Failed to delete reservation: " + t.getMessage());
            }
        });
    }

}
