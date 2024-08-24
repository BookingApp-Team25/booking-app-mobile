package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.util.Pair;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.AccommodationHostListAdapter;
import com.example.bookingapp.adapters.AccommodationLogAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityHostLogsNavBarBinding;
import com.example.bookingapp.dto.AccommodationLog;
import com.example.bookingapp.dto.AccommodationLogCollection;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.datepicker.CalendarConstraints;
import com.google.android.material.datepicker.DateValidatorPointForward;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.navigation.NavigationView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostLogsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AccommodationLogAdapter.AnnualLogListener {

    ActivityHostLogsNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private EditText startDateEditText;
    private EditText endDateEditText;
    private Button selectDateButton;
    private Button generateButton;
    private LocalDate selectedDateStart;
    private LocalDate selectedDateEnd;
    private List<AccommodationLog> generatedLogs;
    private ListView generatedLogsListView;
    AccommodationLogAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        generatedLogs = new ArrayList<>();
        super.onCreate(savedInstanceState);
        binding = ActivityHostLogsNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityHostLogs.toolbarHost);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityHostLogs.toolbarHost;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        startDateEditText = binding.activityHostLogs.activityHostLogsContent.logStartDateEditText;
        endDateEditText = binding.activityHostLogs.activityHostLogsContent.logEndDateEditText;
        selectDateButton = binding.activityHostLogs.activityHostLogsContent.accommodationLogSelectDateButton;
        selectDateButton.setOnClickListener(view -> openDateRangePicker());

        generatedLogsListView = binding.activityHostLogs.activityHostLogsContent.generatedLogsListView;
        adapter = new AccommodationLogAdapter(HostLogsActivity.this,generatedLogs, HostLogsActivity.this);
        generatedLogsListView.setAdapter(adapter);

        generateButton = binding.activityHostLogs.activityHostLogsContent.accommodationLogGenerateLogsButton;
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateLogs();
            }
        });

    }
    private void generateLogs(){
        DateTimeFormatter customFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String startDateStr = startDateEditText.getText().toString();
        String endDateStr = endDateEditText.getText().toString();
        LocalDate startDate = LocalDate.parse(startDateStr, customFormatter);
        LocalDate endDate = LocalDate.parse(endDateStr,customFormatter);
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atStartOfDay();

        ZonedDateTime zonedStartDateTime = startDateTime.atZone(ZoneId.systemDefault());
        ZonedDateTime zonedEndDateTime = endDateTime.atZone(ZoneId.systemDefault());

        String isoStartDateTime = zonedStartDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        String isoEndDateTime = zonedEndDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
        Call<AccommodationLogCollection> call = ClientUtils.accommodationService.generateLogs(UserInfo.getUsername(),isoStartDateTime,isoEndDateTime,UserInfo.getToken());
        call.enqueue(new Callback<AccommodationLogCollection>() {
            @Override
            public void onResponse(Call<AccommodationLogCollection> call, Response<AccommodationLogCollection> response) {
                if(response.code() == 200){
                    generatedLogs.clear();
                    generatedLogs.addAll(response.body().getLogs());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(HostLogsActivity.this, "GENERATE LOGS SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationLogCollection> call, Throwable t) {
                Toast.makeText(HostLogsActivity.this, "GENERATE LOGS ERROR", Toast.LENGTH_SHORT).show();
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

            return true;
        }
        else if(itemId == R.id.itemViewReviews){

            return true;
        }
        else if(itemId == R.id.iconHome){
            Intent intent = new Intent(HostLogsActivity.this, HostMainActivity.class);
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
            case R.id.nav_account:
                Intent intent = new Intent(this,  AccountActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                Call<MessageResponse> call= ClientUtils.authService.logout(UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(HostLogsActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT);
                            startActivity(new Intent(HostLogsActivity.this,LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void openDateRangePicker() {
        // Create Date Range Picker
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select a date range");

        // Optional: Set constraints (e.g., only future dates)
        CalendarConstraints.Builder constraintsBuilder = new CalendarConstraints.Builder();
        //constraintsBuilder.setValidator(DateValidatorPointForward.now());
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
    public void generateAnnualLog(UUID accommodationId,String accommodationName) {
        Intent intent = new Intent(HostLogsActivity.this, AnualLogActivity.class);
        intent.putExtra("accommodationId", accommodationId.toString());
        intent.putExtra("accommodationName",accommodationName);
        startActivity(intent);
    }
}