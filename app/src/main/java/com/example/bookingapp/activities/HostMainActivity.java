package com.example.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.AccommodationsActivity;
import com.example.bookingapp.activities.AccountActivity;
import com.example.bookingapp.activities.GuestReservationsActivity;
import com.example.bookingapp.activities.NotificationsActivity;
import com.example.bookingapp.adapters.AccommodationHostListAdapter;
import com.example.bookingapp.adapters.AccommodationListAdapter;
import com.example.bookingapp.adapters.ReservationDateAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityHostMainNavBarBinding;
import com.example.bookingapp.dto.AccommodationRequest;
import com.example.bookingapp.dto.AccommodationSummaryCollectionResponse;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.AccountDetailsResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationResponse;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HostMainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, AccommodationHostListAdapter.AccommodationEditListener, AccommodationHostListAdapter.AccommodationDetailsListener, SensorEventListener {


    private ActivityHostMainNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ListView hostAccommodationsListView;
    AccommodationHostListAdapter adapter;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private static final float SHAKE_THRESHOLD = 12.0f;
    private static final int SHAKE_SLOP_TIME_MS = 500;
    private long lastShakeTime;
    public static UUID hostId;
    private int sortOrder;
    private ArrayList<AccommodationSummaryResponse> accommodationSummaries;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sortOrder = 0;// Najskorije
        super.onCreate(savedInstanceState);
        accommodationSummaries = new ArrayList<>();
        String username = UserInfo.getUsername();
        Call<AccountDetailsResponse> call = ClientUtils.userService.getAccountDetails(UserInfo.getUsername(), UserInfo.getToken());
        call.enqueue(new Callback<AccountDetailsResponse>() {
            @Override
            public void onResponse(Call<AccountDetailsResponse> call, Response<AccountDetailsResponse> response) {
                if(response.code()==200){
                   HostMainActivity.hostId = UUID.fromString(response.body().getId());
                    loadSummaries();
                }
                else{
                    Toast.makeText(HostMainActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsResponse> call, Throwable t) {
                Toast.makeText(HostMainActivity.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
            }
        });

        binding = ActivityHostMainNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityHostMain.toolbarHost);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        hostAccommodationsListView = binding.activityHostMain.activityHostMainContent.hostAccommodationsListView;
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityHostMain.toolbarHost;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        adapter = new AccommodationHostListAdapter(HostMainActivity.this,accommodationSummaries,HostMainActivity.this,HostMainActivity.this);
        hostAccommodationsListView.setAdapter(adapter);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager != null) {
            accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }

        // Register the listener
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }


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
        if (itemId == R.id.itemCreateAccommodation) {
            Intent intent = new Intent(HostMainActivity.this, AccommodationCreationActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.viewReservations){
            Intent intent = new Intent(HostMainActivity.this, HostReservationsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.viewProfile){
            Intent intent = new Intent(HostMainActivity.this, ActivityHostProfile.class);
            intent.putExtra("hostUsername", "d: " + UserInfo.getUsername());
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.iconHome){
            loadSummaries();
            return true;
        }
        else if(itemId == R.id.viewStatistics){
            Intent intent = new Intent(HostMainActivity.this, HostLogsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.viewNotifications){
            Intent intent = new Intent(HostMainActivity.this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.d("Navigation", "Item clicked: " + item.getItemId());
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
                            Toast.makeText(HostMainActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT);
                            startActivity(new Intent(HostMainActivity.this,LoginActivity.class));
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
    private void loadSummaries(){
        List<AccommodationSummaryResponse> newSummaries = new ArrayList<>();
        Call<AccommodationSummaryCollectionResponse> call = ClientUtils.accommodationService.getHostAccommodations(HostMainActivity.hostId.toString(), 0,10,UserInfo.getToken());
        call.enqueue(new Callback<AccommodationSummaryCollectionResponse>() {
            @Override
            public void onResponse(Call<AccommodationSummaryCollectionResponse> call, Response<AccommodationSummaryCollectionResponse> response) {
                if(response.code()==200){
                    Toast.makeText(HostMainActivity.this, "Number of host apartments:" + String.valueOf(response.body().getTotalNumberOfSummaries()), Toast.LENGTH_SHORT).show();
                    accommodationSummaries.clear();
                    accommodationSummaries.addAll(response.body().getSummaries());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(HostMainActivity.this, "FAILED LOADING APPARTMENTS", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationSummaryCollectionResponse> call, Throwable t) {
                Toast.makeText(HostMainActivity.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onEditAccommodation(AccommodationSummaryResponse accommodationSummaryResponse) {
        Log.e("edit Accommodation","Edit accommodation");
        Intent intent = new Intent(HostMainActivity.this, AccommodationCreationActivity.class);
        intent.putExtra("isEdit",true);
        intent.putExtra("accommodationId", accommodationSummaryResponse.getAccommodationId().toString());
        startActivity(intent);
    }

    @Override
    public void onClickAccommodation(String accommodationId) {
        Intent intent = new Intent(HostMainActivity.this,AccommodationsActivity.class);
        intent.putExtra("accommodationId",UUID.fromString(accommodationId));
        startActivity(intent);
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            // Calculate the acceleration magnitude
            float acceleration = (float) Math.sqrt(x * x + y * y + z * z) - SensorManager.GRAVITY_EARTH;

            // Check if the acceleration exceeds the shake threshold
            if (acceleration > SHAKE_THRESHOLD) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastShakeTime > SHAKE_SLOP_TIME_MS) {
                    lastShakeTime = currentTime;
                    onShakeDetected();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
    private void onShakeDetected() {
        Toast.makeText(this, "Shake detected!", Toast.LENGTH_SHORT).show();
        if(sortOrder == 0){
            sortOrder = 1;
            sortAccommodationsOldest();
            adapter.notifyDataSetChanged();
        }
        else{
            sortOrder = 0;
            sortAccommodationsEarliest();
            adapter.notifyDataSetChanged();
        }
    }
    private void sortAccommodationsEarliest(){
        Collections.sort(accommodationSummaries, new Comparator<AccommodationSummaryResponse>() {
            @Override
            public int compare(AccommodationSummaryResponse e1, AccommodationSummaryResponse e2) {
                return e1.getEarliestAvailableDate().compareTo(e2.getEarliestAvailableDate());
            }
        });
    }
    private void sortAccommodationsOldest(){
        Collections.sort(accommodationSummaries, new Comparator<AccommodationSummaryResponse>() {
            @Override
            public int compare(AccommodationSummaryResponse e1, AccommodationSummaryResponse e2) {
                return e2.getEarliestAvailableDate().compareTo(e1.getEarliestAvailableDate());
            }
        });
    }
}