package com.example.bookingapp.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.NotificationHelper;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityHostReservationsNavBarBinding;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationRequest;
import com.example.bookingapp.fragments.HostReservationFragmentInterface;
import com.example.bookingapp.fragments.HostReservationRequestsFragment;
import com.example.bookingapp.fragments.HostReservationsFragment;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostReservationsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityHostReservationsNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private Button reservationsFragmentButton;
    private Button reservationRequestsFragmentButton;
    private Button filterButton;
    private PopupWindow filterPopupWindow;
    LocalDate selectedDateStart;
    LocalDate selectedDateEnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostReservationsNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityHostReservations.toolbarHost);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityHostReservations.toolbarHost;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        reservationsFragmentButton = binding.activityHostReservations.activityHostReservationsContent.hostReservationsButton;
        reservationsFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment();
            }
        });

        reservationRequestsFragmentButton = binding.activityHostReservations.activityHostReservationsContent.hostReservationRequestsButton;
        reservationRequestsFragmentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment();
            }
        });
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.reservationsFragmentContainer, new HostReservationsFragment())
                    .commit();
        }
        filterButton = binding.activityHostReservations.activityHostReservationsContent.hostReservationShowFilterButton;
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFilterPopup(view);
            }
        });


    }
    private void showFilterPopup(View view){
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_filter_reservation_requests, null);
        float density = getResources().getDisplayMetrics().density;
        filterPopupWindow= new PopupWindow(popupView,
                Math.round(350*density),
                Math.round(550*density),
                true);
        filterPopupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        TextView searchByNameTextView = popupView.findViewById(R.id.searchRequestByNameTextView);
        TextView startDateTextView = popupView.findViewById(R.id.searchRequestsStartDateTextView);
        TextView endDateTextView = popupView.findViewById(R.id.searchRequestsEndDateTextView);
        Button addDateButton = popupView.findViewById(R.id.searchReservationsAddDateButton);
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

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        CheckBox waiting = popupView.findViewById(R.id.filterRequestsWaitingCheckBox);
        CheckBox accepted = popupView.findViewById(R.id.filterRequestsAcceptedCheckBox);
        CheckBox rejeceted = popupView.findViewById(R.id.filterRequestsRejectedCheckBox);
        Button filterRequestsButton = popupView.findViewById(R.id.popupReservationRequestsFilterButton);
        filterRequestsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterRequests(searchByNameTextView.getText().toString(),selectedDateStart,selectedDateEnd,accepted.isChecked(),waiting.isChecked(),rejeceted.isChecked());
            }
        });
        Button closeFilterPopupButton = popupView.findViewById(R.id.popupReservationRequestsExitButton);
        closeFilterPopupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterPopupWindow.dismiss();
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_host_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemViewRequests) {
            Intent intent = new Intent(HostReservationsActivity.this, UpdateRequestsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.itemViewReviews){
            Intent intent = new Intent(HostReservationsActivity.this, ReportedReviewsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.iconHome){
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_account:
                Intent intent = new Intent(this,  AccountActivity.class);
                startActivity(intent);
                return true;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void switchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.reservationsFragmentContainer);

        if (currentFragment instanceof HostReservationsFragment) {
            // Replace FragmentA with FragmentB
            fragmentTransaction.replace(R.id.reservationsFragmentContainer, new HostReservationRequestsFragment());
        } else {
            // Replace FragmentB with FragmentA
            fragmentTransaction.replace(R.id.reservationsFragmentContainer, new HostReservationsFragment());
        }

        // Optional: add the transaction to the back stack so the user can navigate back
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
    public boolean isNotificationPermissionGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }
    public void filterRequests(String name,LocalDate startDate, LocalDate endDate, boolean accepted, boolean waiting, boolean rejected){
        FragmentManager fragmentManager = getSupportFragmentManager();
        HostReservationFragmentInterface currentFragment = (HostReservationFragmentInterface) fragmentManager.findFragmentById(R.id.reservationsFragmentContainer);
        currentFragment.filterReservations(name,startDate,endDate,accepted,waiting,rejected);
    }
}