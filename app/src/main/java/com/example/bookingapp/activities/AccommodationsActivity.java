package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityAccommodationsBinding;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;

public class AccommodationsActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAccommodationsBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
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

        // Retrieve the passed AccommodationSummaryResponse object
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("accommodation")) {
            AccommodationSummaryResponse accommodation = intent.getParcelableExtra("accommodation");

            if (accommodation != null) {
                TextView nameTextView = findViewById(R.id.textView6);
                ImageView imageView = findViewById(R.id.imageView3);
                TextView descriptionTextView = findViewById(R.id.textView9);
                //TextView priceTextView = findViewById(R.id.accommodation_price);
                RatingBar ratingTextView = findViewById(R.id.ratingBar);

                nameTextView.setText(accommodation.getName());
                descriptionTextView.setText(accommodation.getDescription());
                //priceTextView.setText("Price per night: " + accommodation.getPrice() + "$");
                ratingTextView.setRating(Float.parseFloat(accommodation.getRating().toString()));

                String photoUrl = accommodation.getPhoto();
                Glide.with(this)
                        .load(photoUrl)
                        .into(imageView);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_guest_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemAccount) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.itemNotificationsView){
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
}
