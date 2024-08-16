package com.example.bookingapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.clients.AccommodationService;
import com.example.bookingapp.network.RetrofitClient;

import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationSearchViewModel extends ViewModel {

    private final MutableLiveData<Collection<AccommodationSummaryResponse>> accommodationsLiveData = new MutableLiveData<>();
    private final AccommodationService apiService;

    public AccommodationSearchViewModel() {
        apiService = ClientUtils.accommodationService;
    }

    public LiveData<Collection<AccommodationSummaryResponse>> getAccommodations() {
        return accommodationsLiveData;
    }

    public void searchAccommodations(String city, String startDate, String endDate, int numberOfPeople) {
        Call<Collection<AccommodationSummaryResponse>> call = apiService.searchAccommodations(city, startDate, endDate, numberOfPeople);
        call.enqueue(new Callback<Collection<AccommodationSummaryResponse>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationSummaryResponse>> call, Response<Collection<AccommodationSummaryResponse>> response) {
                if (response.isSuccessful()) {
                    // Update LiveData with the list of accommodations
                    accommodationsLiveData.setValue(response.body());
                    if (accommodationsLiveData != null)
                        Log.d("AccommodationsSearchViewModel", "Returned with data");
                    else Log.d("AccommodationsSearchViewModel", "Returned with data but accommodationsLiveData is null");
                } else {
                    // Handle error response
                }
            }

            @Override
            public void onFailure(Call<Collection<AccommodationSummaryResponse>> call, Throwable t) {
                // Handle network errors
                Log.e("AccommodationsSearchViewModel", "Failed to fetch accommodations: " + t.getMessage());
            }
        });
    }
}
