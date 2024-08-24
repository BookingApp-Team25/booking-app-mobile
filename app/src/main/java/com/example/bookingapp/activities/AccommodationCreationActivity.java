package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAccommodationCreationBinding;
import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.AccommodationRequest;
import com.example.bookingapp.dto.enums.AccommodationReservationPolicy;
import com.example.bookingapp.dto.enums.PriceCalculationMethod;
import com.example.bookingapp.entities.AccommodationPricelist;
import com.example.bookingapp.entities.DatePeriod;
import com.example.bookingapp.entities.ReservationDatePeriod;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationCreationActivity extends AppCompatActivity {
    public static boolean isEdit;
    public static AccommodationRequest newAccommodationRequest = new AccommodationRequest();
    public static UUID accommodationUUID;
    private ActivityAccommodationCreationBinding binding;
    private EditText accName;
    private EditText accDescription;
    private List<String> amenities;
    private ArrayAdapter<String> amenityAdapter;
    private ListView amenityListView;
    private Button nextCreationPageButton;
    private Button creationPageReturnButton;
    private Button addAmenityButton;
    private EditText addAmenityEditText;
    private EditText minGuestsEditText;
    private EditText maxGuestsEditText;
    private EditText daysBeforeReservationEditText;
    private EditText pricePerNightEditText;
    private EditText weekendPriceTextView;
    private EditText summerPriceTextView;
    private EditText winterPriceTextView;
    private Switch perGuestSwitch;
    private Switch autoReservationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(newAccommodationRequest == null){
            newAccommodationRequest = new AccommodationRequest();
        }
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation_creation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        binding = ActivityAccommodationCreationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        accName = binding.accNameTexview;
        accDescription = binding.accDescriptionTextview;
        minGuestsEditText = binding.minGuestsTextView;
        maxGuestsEditText = binding.maxGuestsTextView;
        daysBeforeReservationEditText = binding.daysBeforeTextView;
        pricePerNightEditText = binding.perNightTextView;
        summerPriceTextView = binding.summerTextView;
        winterPriceTextView = binding.winterTextView;
        weekendPriceTextView = binding.weekendTextView;

        amenities = new ArrayList<>();
        amenityListView = binding.amenityListView;
        amenityAdapter = new ArrayAdapter<>(this, R.layout.amenity_view, R.id.addAmenityEditText, amenities);
        amenityListView.setAdapter(amenityAdapter);
        perGuestSwitch = binding.perGuestSwitch;
        autoReservationSwitch = binding.autoReservationSwitch;

        addAmenityButton = binding.addAmenityButton;
        addAmenityEditText = binding.addAmenityEditText;
        addAmenityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amenityTitle = addAmenityEditText.getText().toString();
                if(amenityTitle.length()>1){
                    amenities.add(amenityTitle);
                    amenityAdapter.notifyDataSetChanged();
                }
            }
        });

        Intent intent = getIntent();
        if(intent != null){
            isEdit = intent.getBooleanExtra("isEdit",false);
        }
        if(isEdit) {
            Toast.makeText(AccommodationCreationActivity.this, "EDITING", Toast.LENGTH_SHORT).show();
            String accommodationId = intent.getStringExtra("accommodationId");
            accommodationUUID = UUID.fromString(accommodationId);
            Call<AccommodationDetailsResponse> call = ClientUtils.accommodationService.getAccommodation(UUID.fromString(accommodationId), UserInfo.getToken());

            call.enqueue(new Callback<AccommodationDetailsResponse>() {
                @Override
                public void onResponse(Call<AccommodationDetailsResponse> call, Response<AccommodationDetailsResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("DEBUG", "onResponse called");
                        AccommodationDetailsResponse accommodation = new AccommodationDetailsResponse(response.body());
                        newAccommodationRequest.setPhotos(accommodation.getPhotos());
                        newAccommodationRequest.setName(accommodation.getName());
                        ArrayList<ReservationDatePeriod> reservationDatePeriods = new ArrayList<>();
                        for (DatePeriod period : accommodation.getAvailability()) {
                            reservationDatePeriods.add(new ReservationDatePeriod(period.getStartDate(), period.getEndDate()));
                        }
                        newAccommodationRequest.setAvailability(reservationDatePeriods);
                        newAccommodationRequest.setLocation(accommodation.getLocation());
                        newAccommodationRequest.setType(accommodation.getType());
                        newAccommodationRequest.setPolicy(accommodation.getPolicy());
                        newAccommodationRequest.setPrice(accommodation.getPrice());
                        newAccommodationRequest.setPricelist(accommodation.getPricelist());
                        newAccommodationRequest.setDescription(accommodation.getDescription());
                        accDescription.setText(newAccommodationRequest.getDescription());
                        newAccommodationRequest.setAmenities(accommodation.getAmenities());
                        newAccommodationRequest.setMinGuests(accommodation.getMinGuests());
                        newAccommodationRequest.setMaxGuests(accommodation.getMaxGuests());
                        newAccommodationRequest.setPriceCalculationMethod(PriceCalculationMethod.PER_GUEST);
                        accName.setText(newAccommodationRequest.getName());
                        accDescription.setText(newAccommodationRequest.getDescription());
                        pricePerNightEditText.setText(String.valueOf(newAccommodationRequest.getPricelist().getDailyPrice()));
                        summerPriceTextView.setText(String.valueOf(newAccommodationRequest.getPricelist().getSummerPrice()));
                        winterPriceTextView.setText(String.valueOf(newAccommodationRequest.getPricelist().getWinterPrice()));
                        weekendPriceTextView.setText(String.valueOf(newAccommodationRequest.getPricelist().getWeekendPrice()));
                        minGuestsEditText.setText(String.valueOf(newAccommodationRequest.getMinGuests()));
                        maxGuestsEditText.setText(String.valueOf(newAccommodationRequest.getMaxGuests()));
                        daysBeforeReservationEditText.setText(String.valueOf(newAccommodationRequest.getDaysBefore()));
                        if(newAccommodationRequest.getPolicy() == AccommodationReservationPolicy.Auto){
                            autoReservationSwitch.setChecked(true);
                        }
                        if(newAccommodationRequest.getPriceCalculationMethod() == PriceCalculationMethod.PER_GUEST){
                            perGuestSwitch.setChecked(true);
                        }
                        for(String amenity : newAccommodationRequest.getAmenities()){
                            amenities.add(amenity);
                        }
                        amenityAdapter.notifyDataSetChanged();

                    } else {
                        Log.e("GRESKA", "Response received but not successful: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<AccommodationDetailsResponse> call, Throwable t) {

                }
            });
        }

        nextCreationPageButton = binding.nextCreationPageButton1;
        nextCreationPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newAccommodationRequest.setName(accName.getText().toString());
                newAccommodationRequest.setDescription(accDescription.getText().toString());
                newAccommodationRequest.setMinGuests(Integer.parseInt(minGuestsEditText.getText().toString()));
                newAccommodationRequest.setMaxGuests(Integer.parseInt(maxGuestsEditText.getText().toString()));
                newAccommodationRequest.setDaysBefore(Integer.parseInt(daysBeforeReservationEditText.getText().toString()));
                newAccommodationRequest.setAmenities(amenities);
                newAccommodationRequest.setPrice(0);
                AccommodationPricelist pricelist = new AccommodationPricelist(Double.parseDouble(pricePerNightEditText.getText().toString()),
                        Double.parseDouble(weekendPriceTextView.getText().toString()),
                        Double.parseDouble(summerPriceTextView.getText().toString()),
                        Double.parseDouble(winterPriceTextView.getText().toString()));
                newAccommodationRequest.setPricelist(pricelist);
                PriceCalculationMethod priceCalculationMethod;
                if(perGuestSwitch.isChecked()){
                    priceCalculationMethod = PriceCalculationMethod.PER_GUEST;
                }
                else{
                    priceCalculationMethod = PriceCalculationMethod.PER_UNIT;
                }
                newAccommodationRequest.setPriceCalculationMethod(priceCalculationMethod);
                newAccommodationRequest.setPolicy(AccommodationReservationPolicy.Manually);
                if(autoReservationSwitch.isChecked()){
                    newAccommodationRequest.setPolicy(AccommodationReservationPolicy.Auto);
                }


                Intent intent = new Intent(AccommodationCreationActivity.this, AccommodationCreationMapActivity.class);
                startActivity(intent);
            }
        });

        creationPageReturnButton = binding.accCreationReturnButton;
        creationPageReturnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccommodationCreationActivity.this, HostMainActivity.class);
                startActivity(intent);
            }
        });

    }
}