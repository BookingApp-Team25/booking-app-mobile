package com.example.bookingapp.activities;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.bookingapp.R;
import com.example.bookingapp.activities.AccommodationsActivity;
import com.example.bookingapp.activities.AccountActivity;
import com.example.bookingapp.activities.GuestReservationsActivity;
import com.example.bookingapp.activities.NotificationsActivity;
import com.example.bookingapp.databinding.ActivityHostMainNavBarBinding;
import com.google.android.material.navigation.NavigationView;

public class HostMainActivity extends AppCompatActivity {


    private ActivityHostMainNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHostMainNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityHostMain.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        toolbar = binding.activityHostMain.toolbar;

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

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
        if (itemId == R.id.itemAccount) {
            Intent intent = new Intent(this, AccountActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.itemAccomodationView){
            Intent intent = new Intent(this,  AccommodationsActivity.class);
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