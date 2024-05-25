package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ImagePagerAdapter;
import com.example.bookingapp.databinding.ActivityAccommodationsBinding;
import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.network.RetrofitClient;
import com.example.bookingapp.network.AccommodationService;
import com.google.android.material.navigation.NavigationView;
import android.view.Menu;
import android.view.MenuItem;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccommodationsActivity extends AppCompatActivity {

    private static final String TAG = "AccommodationsActivity";

    private AppBarConfiguration appBarConfiguration;
    private ActivityAccommodationsBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ViewPager2 viewPager;

    private MapView mapView; // Add MapView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.drawerLayout.activityGuestMainNoContent.toolbar);
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout.drawerLayout;
        navigationView = binding.drawerLayout.navView;
        toolbar = binding.drawerLayout.activityGuestMainNoContent.toolbar;

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        // Retrieve the accommodationId from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("accommodationId")) {
            UUID accommodationId = (UUID) intent.getSerializableExtra("accommodationId");
            Log.d(TAG, "Accommodation ID: " + accommodationId);
            fetchAccommodationDetails(accommodationId);
        } else {
            Log.d(TAG, "No accommodationId found in the intent.");
        }

        // Pressing the button triggers the ReservationActivity
        Button reserveButton = findViewById(R.id.button);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the Reservation Activity
                Intent intent = new Intent(AccommodationsActivity.this, ReservationActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchAccommodationDetails(UUID accommodationId) {
        AccommodationService apiService = RetrofitClient.getClient("http://10.0.2.2:8080/api/").create(AccommodationService.class);
        Call<AccommodationDetailsResponse> call = apiService.getAccommodation(accommodationId);

        call.enqueue(new Callback<AccommodationDetailsResponse>() {
            @Override
            public void onResponse(Call<AccommodationDetailsResponse> call, Response<AccommodationDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Accommodation details retrieved: " + response.body());
                    populateUI(response.body());
                } else {
                    Log.d(TAG, "Response received but not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AccommodationDetailsResponse> call, Throwable t) {
                Log.e(TAG, "API call failed: ", t);
            }
        });
    }

    private void populateUI(AccommodationDetailsResponse accommodation) {
        TextView nameTextView = findViewById(R.id.textView6);
        TextView descriptionTextView = findViewById(R.id.textView9);
        TextView amenitiesTextView = findViewById(R.id.textViewAmenities);
        RatingBar ratingTextView = findViewById(R.id.ratingBar);
        viewPager = findViewById(R.id.viewPager);

        nameTextView.setText(accommodation.getName());
        descriptionTextView.setText(accommodation.getDescription());
        String amenitiesString = "This accommodation includes:\n\t\t\t";
        for (String amenity:accommodation.getAmenities()) {
            amenitiesString += amenity + ", ";
        }
        amenitiesTextView.setText(amenitiesString.substring(0, amenitiesString.length() - 2));
        ratingTextView.setRating((float) accommodation.getRating());

        // Set up ViewPager with images
        List<String> imageUrls = accommodation.getPhotos();
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);

        // Add MapView
        mapView = findViewById(R.id.mapView);
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(new GeoPoint(45.267136, 19.833549));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_guest_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemAccount) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.itemNotificationsView) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.itemReservationView) {
            Intent intent = new Intent(this, GuestReservationsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
