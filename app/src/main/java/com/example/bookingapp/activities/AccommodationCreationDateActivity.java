package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ReservationDateAdapter;
import com.example.bookingapp.clients.DateManagementService;
import com.example.bookingapp.databinding.ActivityAccommodationCreationDateBinding;
import com.example.bookingapp.databinding.ActivityAccommodationCreationMapBinding;
import com.example.bookingapp.entities.ReservationDatePeriod;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class AccommodationCreationDateActivity extends AppCompatActivity implements ReservationDateAdapter.ReservedDateDeleteListener {
    private Button datePickerButton;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private ArrayList<ReservationDatePeriod> reservationDatePeriods;
    private Button addReservedDateButton;
    private LocalDate selectedDateStart;
    private LocalDate selectedDateEnd;
    private ReservationDateAdapter reservationDateAdapter;
    private ListView reservedDatesListView;
    private Button nextPageButton;
    private Switch weekend;
    private Switch summer;
    private Switch winter;

    ActivityAccommodationCreationDateBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation_creation_date);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityAccommodationCreationDateBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        datePickerButton = binding.pickDateRangeButton;
        datePickerButton.setOnClickListener(view -> openDateRangePicker());
        startDateEditText = binding.startDateEditText;
        endDateEditText = binding.endDateEditText;
        reservationDatePeriods = new ArrayList<>();

        reservationDateAdapter = new ReservationDateAdapter(AccommodationCreationDateActivity.this,reservationDatePeriods,AccommodationCreationDateActivity.this);
        reservedDatesListView = binding.accCreationDateListView;
        reservedDatesListView.setAdapter(reservationDateAdapter);
        winter = binding.creationDateWinterSwitch;
        summer = binding.creationDateSummerSwitch;
        weekend = binding.creationDateWeekendSwitch;

        if(AccommodationCreationActivity.isEdit){
            for(ReservationDatePeriod reservationDatePeriod : AccommodationCreationActivity.newAccommodationRequest.getAvailability()){
                reservationDatePeriods.add(reservationDatePeriod);
            }
            reservationDateAdapter.notifyDataSetChanged();
        }


        addReservedDateButton = binding.accCreationDateAddButton;
        addReservedDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startDateEditText.getText().length()<1){
                    return;
                }
                else{
                    ReservationDatePeriod reservedPeriod = new ReservationDatePeriod();
                    reservedPeriod.setStartDate(selectedDateStart);
                    reservedPeriod.setEndDate(selectedDateEnd);
                    reservedPeriod.setWinterApplied(winter.isChecked());//HARDCODED FOR NOW
                    reservedPeriod.setSummerApplied(summer.isChecked());
                    reservedPeriod.setWeekendApplied(weekend.isChecked());

                    if(isPeriodOverlaping(reservedPeriod)){
                        Toast.makeText(AccommodationCreationDateActivity.this, "Period is overlapping", Toast.LENGTH_LONG).show();
                    }
                    else{
                        reservationDatePeriods.add(reservedPeriod);
                        reservationDateAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        nextPageButton = binding.creationPageButton3;
        nextPageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccommodationCreationActivity.newAccommodationRequest.setAvailability(reservationDatePeriods);
                Intent intent = new Intent(AccommodationCreationDateActivity.this, AccommodationCreationPhotosActivity.class);
                startActivity(intent);
            }
        });

    }
    private void openDateRangePicker() {
        // Create Date Range Picker
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

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

            LocalDate startDate = Instant.ofEpochMilli(startDateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate endDate = Instant.ofEpochMilli(endDateInMillis).atZone(ZoneId.systemDefault()).toLocalDate();

            selectedDateStart = startDate;
            selectedDateEnd = endDate;

            String formattedStartDate = formatter.format(startDate);
            String formattedEndDate = formatter.format(endDate);
            startDateEditText.setText(formattedStartDate);
            endDateEditText.setText(formattedEndDate);
        });
    }

    @Override
    public void onDeleteReservedDate(ReservationDatePeriod reservedDate) {
        reservationDatePeriods.remove(reservedDate);
        reservationDateAdapter.notifyDataSetChanged();
    }
    private boolean isPeriodOverlaping(ReservationDatePeriod newDatePeriod){
        DateManagementService dateManagementService = new DateManagementService();
        for (ReservationDatePeriod datePeriod : reservationDatePeriods){
            if(dateManagementService.doPeriodsOverlap(newDatePeriod.getStartDate(),newDatePeriod.getEndDate(),datePeriod.getStartDate(),datePeriod.getEndDate())){
                return true;
            }
        }
        return false;
    }
}