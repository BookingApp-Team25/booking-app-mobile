package com.example.bookingapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.clients.AccommodationService;
import com.example.bookingapp.clients.AccommodationService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationSearchViewModel extends ViewModel {

    private final MutableLiveData<Collection<AccommodationSummaryResponse>> accommodationsLiveData = new MutableLiveData<>();
    //private final AccommodationService apiService;

    public AccommodationSearchViewModel() {
      //  apiService = RetrofitClient.getClient("http://192.168.1.39/api/").create(AccommodationService.class); //http://10.0.2.2:8080/api/
    }

    public LiveData<Collection<AccommodationSummaryResponse>> getAccommodations() {
        return accommodationsLiveData;
    }

    public void searchAccommodations(String city, String startDate, String endDate, int numberOfPeople) {
        Log.d("AccommodationSearchViewModel searchAccommodations", "Just before sending backend call.");
        Log.d("AccommodationSearchViewModel", "Parameters - City: " + city + ", StartDate: " + startDate + ", EndDate: " + endDate + ", NumberOfPeople: " + numberOfPeople);

        // Define the format of the incoming date
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        // Define the desired format
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getDefault()); // Ensure it includes the offset

        String formattedStartDate;
        String formattedEndDate;

        try {
            // Parse the incoming date and format it to the desired format
            formattedStartDate = outputFormat.format(inputFormat.parse(startDate));
            formattedEndDate = outputFormat.format(inputFormat.parse(endDate));
        } catch (Exception e) {
            Log.e("AccommodationSearchViewModel", "Error parsing dates", e);
            return; // Exit if there is a date parsing error
        }

        Call<Collection<AccommodationSummaryResponse>> call = ClientUtils.accommodationService.searchAccommodations(city, formattedStartDate, formattedEndDate, numberOfPeople); //apiService.searchAccommodations(city, formattedStartDate, formattedEndDate, numberOfPeople);
        call.enqueue(new Callback<Collection<AccommodationSummaryResponse>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationSummaryResponse>> call, Response<Collection<AccommodationSummaryResponse>> response) {
                Log.d("AccommodationSearchViewModel searchAccommodations", "Inside onResponse.");
                if (response.isSuccessful()) {
                    Collection<AccommodationSummaryResponse> accommodations = response.body();
                    accommodationsLiveData.setValue(accommodations);

                    if (accommodations != null) {
                        for (AccommodationSummaryResponse accommodation : accommodations) {
                            Log.d("AccommodationsSearchViewModel", accommodation.toString());
                        }
                    }
                } else {
                    Log.d("AccommodationSearchViewModel onResponse", "response.isSuccessful() = false");
                    Log.d("AccommodationSearchViewModel onResponse", "Error message: " + response.message());
                    try {
                        Log.d("AccommodationSearchViewModel onResponse", "Error body: " + response.errorBody().string());
                    } catch (IOException e) {
                        Log.e("AccommodationSearchViewModel onResponse", "Error reading error body", e);
                    }
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
