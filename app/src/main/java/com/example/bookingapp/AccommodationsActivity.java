package com.example.bookingapp;

import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.core.view.WindowCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.bookingapp.databinding.ActivityAccommodationsBinding;

public class AccommodationsActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private ActivityAccommodationsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}
