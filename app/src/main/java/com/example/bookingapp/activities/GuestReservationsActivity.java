package com.example.bookingapp.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.GuestReservationsViewModel;
import com.example.bookingapp.R;
import com.example.bookingapp.adapters.GuestReservationsAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.AccountDetailsResponse;
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestReservationsActivity extends AppCompatActivity implements GuestReservationsAdapter.OnDeleteClickListener {

    private GuestReservationsViewModel viewModel;
    private GuestReservationsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_reservations);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewReservations);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new GuestReservationsAdapter(new ArrayList<>(), new HashMap<>(),this);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(GuestReservationsViewModel.class);

        viewModel.getReservations().observe(this, reservations -> {
            if (reservations != null && !reservations.isEmpty()) {
                // Ensure accommodations are fetched before updating the adapter
                viewModel.getAccommodationNames().observe(this, accommodationNames -> {
                    if (accommodationNames != null && !accommodationNames.isEmpty()) {
                        adapter.setReservationList(reservations);
                        adapter.setAccommodationNames(accommodationNames);
                        adapter.notifyDataSetChanged();
                    }
                });
            }
        });

        fetchAccountDetails();
    }

    public void fetchAccountDetails() {
        // Fetching user details to get the guest ID
        ClientUtils.userService.getAccountDetails(UserInfo.getUsername(), UserInfo.getToken()).enqueue(new Callback<AccountDetailsResponse>() {
            @Override
            public void onResponse(Call<AccountDetailsResponse> call, Response<AccountDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String guestId = response.body().getId();
                    Log.d("GuestReservationsActivity", "Fetched guest ID: " + guestId);  // Added logging
                    viewModel.fetchReservations(guestId, 0, 10, UserInfo.getToken());
                } else {
                    Log.e("GuestReservations", "Failed to fetch user details: " + response.message());
                    Toast.makeText(GuestReservationsActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsResponse> call, Throwable t) {
                Log.e("GuestReservations", "Failed to fetch user details: " + t.getMessage());
                Toast.makeText(GuestReservationsActivity.this, "Failed to fetch user details", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(String reservationId) {
        viewModel.deleteReservation(reservationId, UserInfo.getToken());
        adapter.notifyDataSetChanged(); // potentially used to refresh feed
    }
}
