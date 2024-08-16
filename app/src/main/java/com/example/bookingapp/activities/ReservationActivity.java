package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.NotificationHelper;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.dto.AccountDetailsResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationRequest;
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.entities.DatePeriod;
import com.example.bookingapp.clients.ReservationService;
import com.example.bookingapp.security.UserInfo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservationActivity extends AppCompatActivity {

    private DatePicker datePickerStart;
    private DatePicker datePickerEnd;
    private Button confirmReservationButton;
    private String hostUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        DatePicker startDatePicker = findViewById(R.id.startDatePicker);
        DatePicker endDatePicker = findViewById(R.id.endDatePicker);
        NumberPicker numberPicker = findViewById(R.id.numberPicker);
        Button buttonConfirmReservation = findViewById(R.id.buttonConfirmReservation);

        Intent intent = getIntent();
        if (intent != null) {
            // Set NumberPicker values

            ArrayList<DatePeriod> availability = intent.getParcelableArrayListExtra("availability");
            int minGuests = intent.getIntExtra("minGuests", 0);
            int maxGuests = intent.getIntExtra("maxGuests", 0);
            String hostId = intent.getStringExtra("hostId");
            String accommodationId = intent.getStringExtra("accommodationId");

            // also add date stting

            numberPicker.setMinValue(minGuests);
            numberPicker.setMaxValue(maxGuests);
            numberPicker.setWrapSelectorWheel(true);

            buttonConfirmReservation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int startDay = startDatePicker.getDayOfMonth();
                    int startMonth = startDatePicker.getMonth();
                    int startYear = startDatePicker.getYear();

                    int endDay = endDatePicker.getDayOfMonth();
                    int endMonth = endDatePicker.getMonth();
                    int endYear = endDatePicker.getYear();

                    int numberOfPeople = numberPicker.getValue();

                    LocalDate startDate = LocalDate.of(startYear, startMonth + 1, startDay);
                    LocalDate endDate = LocalDate.of(endYear, endMonth + 1, endDay);

                    DatePeriod reservedDate = new DatePeriod(startDate, endDate);

                    String reservationDetails = "Start Date: " + startDay + "/" + (startMonth + 1) + "/" + startYear
                            + ", End Date: " + endDay + "/" + (endMonth + 1) + "/" + endYear
                            + ", Number of People: " + numberOfPeople;

                    Log.d("ReservationActivity", reservationDetails);

                    // Show a toast to confirm the reservation
                    Toast.makeText(ReservationActivity.this, "Reservation confirmed:\n" + reservationDetails, Toast.LENGTH_SHORT).show();

                    ReservationRequest reservationRequest = new ReservationRequest(
                            UUID.randomUUID(), // Generate a new reservation ID
                            UUID.fromString("123e4567-e89b-12d3-a456-426614174001"),
                            UUID.fromString(hostId),
                            UUID.fromString(accommodationId),
                            ReservationStatus.ONGOING,
                            reservedDate,
                            1000 // Replace with actual price calculation logic
                    );

                    makeReservation(reservationRequest);
                }
            });
        }
    }

    private void makeReservation(ReservationRequest reservationRequest) {
        ReservationService apiService = ClientUtils.reservationService;
        Call<AccountDetailsResponse> callHost = ClientUtils.userService.getHostDetails(reservationRequest.getHostId().toString(),UserInfo.getToken());
        callHost.enqueue(new Callback<AccountDetailsResponse>() {
            @Override
            public void onResponse(Call<AccountDetailsResponse> call, Response<AccountDetailsResponse> response) {
                if(response.code() == 200){
                    hostUsername = response.body().getUsername();
                }
                else{
                    Toast.makeText(ReservationActivity.this, "Server failed to get host username", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsResponse> call, Throwable t) {
                Toast.makeText(ReservationActivity.this, "Failed to get host username", Toast.LENGTH_SHORT).show();
            }
        });
        Call<MessageResponse> call = apiService.createReservation(reservationRequest, UserInfo.getToken());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();
                    if (messageResponse != null) {
                        Toast.makeText(ReservationActivity.this, "Reservation confirmed: " + messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        NotificationRequest notificationRequest = new NotificationRequest(hostUsername,"New reservation,Korisnik " + UserInfo.getUsername() + " je poslao zahtev za rezervaciju", LocalDateTime.now(),false);
                        NotificationHelper.createAndSaveNotification(ReservationActivity.this,notificationRequest);
                    }
                } else {
                    Toast.makeText(ReservationActivity.this, "Failed to make reservation", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(ReservationActivity.this, "Error making a reservation: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
