package com.example.bookingapp.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.bookingapp.Accommodation;
import com.example.bookingapp.R;
import com.example.bookingapp.activities.AccountActivity;
import com.example.bookingapp.activities.GuestReservationsActivity;
import com.example.bookingapp.activities.NotificationsActivity;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.clients.AccommodationService;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityGuestMainNavBarBinding;
import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.AccommodationSummaryCollectionResponse;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.HostReservationResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.enums.ReservationStatus;
import com.example.bookingapp.entities.DatePeriod;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AccommodationListAdapter.AddFavouriteListener, AccommodationListAdapter.AccommodationDetailsListener {

    private ActivityGuestMainNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ListView accommodationsListView;
    private AccommodationListAdapter adapter;
    private ArrayList<AccommodationSummaryResponse> accommodations;
    private ArrayList<AccommodationDetailsResponse> accommodationDetails;
    private PopupWindow filterPopupWindow;
    private PopupWindow searchPopupWindow;
    private Button searchButton;
    private Button filterButton;
    private LocalDate selectedDateStart;
    private LocalDate selectedDateEnd;
    private AccommodationDetailsResponse accommodation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestMainNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityGuestMain.toolbarGuest);
        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityGuestMain.toolbarGuest;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        accommodations = new ArrayList<>();
        accommodationsListView = binding.activityGuestMain.activityGuestMainContent.guestMainAccommodationViewList;
        adapter = new AccommodationListAdapter(GuestMainActivity.this,accommodations,GuestMainActivity.this,GuestMainActivity.this);
        accommodationsListView.setAdapter(adapter);
        fetchAccommodations();

        searchButton = binding.activityGuestMain.activityGuestMainContent.guestMainSearchButton;
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSearchPopup(view);
            }
        });
        filterButton = binding.activityGuestMain.activityGuestMainContent.guestMainFilterButton;
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup(view);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_guest_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.itemNotificationsView){
            Intent intent = new Intent(this,  NotificationsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.itemReservationView){
            Intent intent = new Intent(this,  GuestReservationsActivity.class);
            startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                Call<MessageResponse> call= ClientUtils.authService.logout(UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(GuestMainActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT);
                            startActivity(new Intent(GuestMainActivity.this,LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
                break;
            case R.id.nav_account:
                startActivity(new Intent(this,AccountActivity.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onAddFavourite(String guestUsername, UUID accommodationId) {
        Call<Boolean> call = ClientUtils.accommodationService.addFavouriteAccommodation(guestUsername,accommodationId,UserInfo.getToken());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200){
                    Toast.makeText(GuestMainActivity.this, "added to favourites", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(GuestMainActivity.this, "ADD FAVOURITE SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(GuestMainActivity.this, "ADD FAVOURITE ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void fetchAccommodations() {
        AccommodationService service = ClientUtils.accommodationService;
        service.getAllApprovedAccommodations().enqueue(new Callback<AccommodationSummaryCollectionResponse>() {
            @Override
            public void onResponse(Call<AccommodationSummaryCollectionResponse> call, Response<AccommodationSummaryCollectionResponse> response) {
                if (response.isSuccessful()) {
                    Collection<AccommodationSummaryResponse> receivedAccommodations = response.body().getSummaries();
                    accommodations.clear();
                    accommodations.addAll(receivedAccommodations);
                    adapter.notifyDataSetChanged();

                    // Log the received accommodations
                    for (AccommodationSummaryResponse accommodation : receivedAccommodations) {
                        Log.d("AccommodationsPageViewModel", "Received accommodation: " + accommodation);
                    }
                }
                else{
                    Toast.makeText(GuestMainActivity.this, "FETCH ACCOMMODATIONS SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationSummaryCollectionResponse> call, Throwable t) {
                // Handle the error
                Toast.makeText(GuestMainActivity.this, "FETCH ACCOMMODATIONS ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void showFilterPopup(View view) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_accommodation_filter, null);
        float density = getResources().getDisplayMetrics().density;
        filterPopupWindow = new PopupWindow(popupView,
                Math.round(350 * density),
                Math.round(700 * density),
                true);
        filterPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        EditText startDateTextView = popupView.findViewById(R.id.accommodationFilterStartDateEditText);
        EditText endDateTextView = popupView.findViewById(R.id.accommodationFilterEndDateEditText);
        EditText locationTextView = popupView.findViewById(R.id.accommodationFilterCityEditText);
        EditText guestNumberTextView = popupView.findViewById(R.id.accommodationFilterGuestNumberEditText);
        EditText amenitiesTextView = popupView.findViewById(R.id.accommodationFilterAmenitiesEditText);
        EditText typeTextView = popupView.findViewById(R.id.accommodationFilterTypeEditText);
        EditText minPriceTextView = popupView.findViewById(R.id.accommodationFilterMinPrice);
        EditText maxPriceTextView = popupView.findViewById(R.id.accommodationFilterMaxPrice);
        Button addDateButton = popupView.findViewById(R.id.accommodationFilterAddDateButton);
        Button cancelButton = popupView.findViewById(R.id.accommodationFilterCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPopupWindow.dismiss();
            }
        });
        Button filterButton = popupView.findViewById(R.id.accommodationFilterSearchButton);
        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDateStart = LocalDate.MIN;
                selectedDateEnd = LocalDate.MAX;
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Select a date range");

                // Optional: Set constraints (e.g., only future dates)
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setValidator(DateValidatorPointForward.now());
                builder.setCalendarConstraints(constraintsBuilder.build());

                final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

                // Show the picker
                picker.show(getSupportFragmentManager(), picker.toString());

                // Handle the positive button click
                picker.addOnPositiveButtonClickListener(selection -> {
                    Long startDateInMillis = selection.first;
                    Long endDateInMillis = selection.second;

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    LocalDate startDate = Instant.ofEpochMilli(startDateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = Instant.ofEpochMilli(endDateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();

                    selectedDateStart = startDate;
                    selectedDateEnd = endDate;

                    String formattedStartDate = formatter.format(startDate);
                    String formattedEndDate = formatter.format(endDate);
                    startDateTextView.setText(formattedStartDate);
                    endDateTextView.setText(formattedEndDate);
                });
            }
        });
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberOfGuests = 0;
                if (!(guestNumberTextView.getText().length() < 1)) {
                    numberOfGuests = Integer.parseInt(guestNumberTextView.getText().toString());
                }
                LocalTime localTime = LocalTime.now();
                ZoneOffset offset = ZoneOffset.UTC;
                OffsetDateTime offsetDateTime1 = OffsetDateTime.of(selectedDateStart, localTime, offset);
                OffsetDateTime offsetDateTime2 = OffsetDateTime.of(selectedDateEnd, localTime, offset);
                String formattedDateTime1 = offsetDateTime1.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                String formattedDateTime2 = offsetDateTime2.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                Call<Collection<AccommodationSummaryResponse>> call = ClientUtils.accommodationService.searchAccommodationsFiltered(locationTextView.getText().toString(),formattedDateTime1,formattedDateTime2,numberOfGuests,
                                                                                                                                    amenitiesTextView.getText().toString(),typeTextView.getText().toString(),
                                                                                                                                    Double.parseDouble(minPriceTextView.getText().toString()),
                                                                                                                                    Double.parseDouble(maxPriceTextView.getText().toString()));
                call.enqueue(new Callback<Collection<AccommodationSummaryResponse>>() {
                    @Override
                    public void onResponse(Call<Collection<AccommodationSummaryResponse>> call, Response<Collection<AccommodationSummaryResponse>> response) {
                        if(response.code() == 200){
                            accommodations.clear();
                            accommodations.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(GuestMainActivity.this, "FILTER SERVER ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Collection<AccommodationSummaryResponse>> call, Throwable t) {
                        Toast.makeText(GuestMainActivity.this, "FILTER ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
                filterPopupWindow.dismiss();
            }
        });
    }
    private void showSearchPopup(View view) {

        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_accommodation_search, null);
        float density = getResources().getDisplayMetrics().density;
        searchPopupWindow = new PopupWindow(popupView,
                Math.round(350 * density),
                Math.round(550 * density),
                true);
        searchPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        EditText startDateTextView = popupView.findViewById(R.id.accommodationSearchStartDateEditText);
        EditText endDateTextView = popupView.findViewById(R.id.accommodationSearchEndDateEditText);
        EditText locationTextView = popupView.findViewById(R.id.accommodationSearchCityEditText);
        EditText guestNumberTextView = popupView.findViewById(R.id.accommodationSearchGuestNumberEditText);
        Button addDateButton = popupView.findViewById(R.id.accommodationSearchAddDateButton);
        Button cancelButton = popupView.findViewById(R.id.accommodationSearchCancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchPopupWindow.dismiss();
            }
        });
        Button searchButton = popupView.findViewById(R.id.accommodationSearchSearchButton);
        addDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectedDateStart = LocalDate.MIN;
                selectedDateEnd = LocalDate.MAX;
                MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
                builder.setTitleText("Select a date range");

                // Optional: Set constraints (e.g., only future dates)
                CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
                constraintsBuilder.setValidator(DateValidatorPointForward.now());
                builder.setCalendarConstraints(constraintsBuilder.build());

                final MaterialDatePicker<Pair<Long, Long>> picker = builder.build();

                // Show the picker
                picker.show(getSupportFragmentManager(), picker.toString());

                // Handle the positive button click
                picker.addOnPositiveButtonClickListener(selection -> {
                    Long startDateInMillis = selection.first;
                    Long endDateInMillis = selection.second;

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

                    LocalDate startDate = Instant.ofEpochMilli(startDateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
                    LocalDate endDate = Instant.ofEpochMilli(endDateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();

                    selectedDateStart = startDate;
                    selectedDateEnd = endDate;

                    String formattedStartDate = formatter.format(startDate);
                    String formattedEndDate = formatter.format(endDate);
                    startDateTextView.setText(formattedStartDate);
                    endDateTextView.setText(formattedEndDate);
                });
            }
        });
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int numberOfGuests = 0;
                if (!(guestNumberTextView.getText().length() < 1)) {
                    numberOfGuests = Integer.parseInt(guestNumberTextView.getText().toString());
                }
                LocalTime localTime = LocalTime.now();
                ZoneOffset offset = ZoneOffset.UTC;
                OffsetDateTime offsetDateTime1 = OffsetDateTime.of(selectedDateStart, localTime, offset);
                OffsetDateTime offsetDateTime2 = OffsetDateTime.of(selectedDateEnd, localTime, offset);
                String formattedDateTime1 = offsetDateTime1.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                String formattedDateTime2 = offsetDateTime2.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);

                Call<Collection<AccommodationSummaryResponse>> call = ClientUtils.accommodationService.searchAccommodations(locationTextView.getText().toString(),formattedDateTime1,formattedDateTime2,numberOfGuests);
                call.enqueue(new Callback<Collection<AccommodationSummaryResponse>>() {
                    @Override
                    public void onResponse(Call<Collection<AccommodationSummaryResponse>> call, Response<Collection<AccommodationSummaryResponse>> response) {
                        if(response.code() == 200){
                            accommodations.clear();
                            accommodations.addAll(response.body());
                            adapter.notifyDataSetChanged();
                        }
                        else{
                            Toast.makeText(GuestMainActivity.this, "SEARCH SERVER ERROR", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Collection<AccommodationSummaryResponse>> call, Throwable t) {
                        Toast.makeText(GuestMainActivity.this, "SEARCH ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            searchPopupWindow.dismiss();
            }
        });
    }
    private void getAccommodationDetails(AccommodationSummaryResponse accommodationSummary){
        Call<AccommodationDetailsResponse> call = ClientUtils.accommodationService.getAccommodation(accommodationSummary.getAccommodationId(),UserInfo.getToken());
        call.enqueue(new Callback<AccommodationDetailsResponse>() {
            @Override
            public void onResponse(Call<AccommodationDetailsResponse> call, Response<AccommodationDetailsResponse> response) {
                if(response.isSuccessful()){
                    accommodation = response.body();
                }
                else{
                    Toast.makeText(GuestMainActivity.this, "FETCH ACCOMMODATION DETAILS SERVER ERROR", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onFailure(Call<AccommodationDetailsResponse> call, Throwable t) {
                Toast.makeText(GuestMainActivity.this, "FETCH ACCOMMODATION DETAILS ERROR", Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    @Override
    public void onViewDetails(String accommodationId) {
        Intent intent = new Intent(GuestMainActivity.this,AccommodationsActivity.class);
        intent.putExtra("accommodationId",UUID.fromString(accommodationId));
        startActivity(intent);
    }
}
