//package com.example.bookingapp;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//public class AccommodationsPageViewModel extends ViewModel {
//    private final MutableLiveData<String> searchText;
//    public AccommodationsPageViewModel(){
//        searchText = new MutableLiveData<>();
//        searchText.setValue("This is search help!");
//    }
//    public LiveData<String> getText(){
//        return searchText;
//    }
//}

package com.example.bookingapp;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bookingapp.network.AccommodationService;
import com.example.bookingapp.network.RetrofitClient;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import com.example.bookingapp.dto.AccommodationSummaryResponse;

public class AccommodationsPageViewModel extends ViewModel {
    private final MutableLiveData<String> searchText;
    private final MutableLiveData<Collection<AccommodationSummaryResponse>> accommodations;

    public AccommodationsPageViewModel() {
        searchText = new MutableLiveData<>();
        accommodations = new MutableLiveData<>();
        searchText.setValue("This is search help!");
    }

    public LiveData<String> getText() {
        return searchText;
    }

    public LiveData<Collection<AccommodationSummaryResponse>> getAccommodations() {
        return accommodations;
    }

    public void fetchAccommodations() {
        AccommodationService service = RetrofitClient.getClient("http://192.168.1.39:8080/api/") //http://10.0.2.2:8080/api/
                .create(AccommodationService.class);
        service.getAllAccommodations().enqueue(new Callback<Collection<AccommodationSummaryResponse>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationSummaryResponse>> call, Response<Collection<AccommodationSummaryResponse>> response) {
                if (response.isSuccessful()) {
                    Collection<AccommodationSummaryResponse> receivedAccommodations = response.body();
                    accommodations.setValue(receivedAccommodations);

                    // Log the received accommodations
                    for (AccommodationSummaryResponse accommodation : receivedAccommodations) {
                        Log.d("AccommodationsPageViewModel", "Received accommodation: " + accommodation);
                    }
                }
            }

            @Override
            public void onFailure(Call<Collection<AccommodationSummaryResponse>> call, Throwable t) {
                // Handle the error
                Log.e("AccommodationsPageViewModel", "Failed to fetch accommodations: " + t.getMessage());
            }
        });
    }
}
