package com.example.bookingapp.activities;

import static com.example.bookingapp.security.UserInfo.getToken;

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
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.entities.DatePeriod;
import com.example.bookingapp.network.ReservationService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ReservationActivity extends AppCompatActivity {

    private DatePicker datePickerStart;
    private DatePicker datePickerEnd;
    private Button confirmReservationButton;

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
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            String token = getToken();
            Request.Builder requestBuilder = original.newBuilder()
                    .header("Authorization", token);
            Request request = requestBuilder.build();
            return chain.proceed(request);
        });

        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, typeOfSrc, context) ->
                        src == null ? null : new JsonPrimitive(src.toString()))
                .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, typeOfT, context) ->
                        LocalDate.parse(json.getAsString(), DateTimeFormatter.ISO_LOCAL_DATE))
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080/api/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient.build())
                .build();

        ReservationService apiService = retrofit.create(ReservationService.class);
        Call<MessageResponse> call = apiService.createReservation(reservationRequest);
        Log.d("ReservationRequest", reservationRequest.toString());

        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    MessageResponse messageResponse = response.body();
                    if (messageResponse != null) {
                        Toast.makeText(ReservationActivity.this, "Reservation confirmed: " + messageResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
