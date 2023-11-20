package com.example.bookingapp;

import android.content.Intent;
import android.os.Bundle;

import com.example.bookingapp.databinding.ActivityGuestMainNavBarBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookingapp.databinding.ActivityAccommodationsBinding;

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

        if(actionBar != null){
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
