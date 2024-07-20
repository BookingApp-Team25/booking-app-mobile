package com.example.bookingapp.activities;

import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookingapp.databinding.ActivityHostProfileBinding;

import com.example.bookingapp.R;

public class HostProfileActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityHostProfileBinding binding;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHostProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.drawerLayout.activityGuestMainNoContent.toolbar);
        ActionBar actionBar = getSupportActionBar();
        drawer = binding.drawerLayout.drawerLayout;
        navigationView = binding.drawerLayout.navView;
        toolbar = binding.drawerLayout.activityGuestMainNoContent.toolbar;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();
    }
}