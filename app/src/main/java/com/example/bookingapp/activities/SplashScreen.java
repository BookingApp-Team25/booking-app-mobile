package com.example.bookingapp.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.bookingapp.R;

public class SplashScreen extends AppCompatActivity {

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        handler.postDelayed(() -> {
            if (isConnectedToInternet()) {
                startLoginActivity();
            } else {
                showInternetNotConnectedMessage();
            }
        }, 3 * 1000); // wait for 5 seconds
    }

    private void startLoginActivity() {
        Intent i = new Intent(SplashScreen.this, LoginActivity.class);
        startActivity(i);
        finish();
    }

    private boolean isConnectedToInternet() {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getActiveNetworkInfo() != null
                && connectivityManager.getActiveNetworkInfo().isConnected();
    }

    private void showInternetNotConnectedMessage() {
        Toast.makeText(this, "Device is not connected to the internet.", Toast.LENGTH_LONG).show();

        // Display an alert dialog to connect to Wi-Fi
        new AlertDialog.Builder(this)
                .setTitle("Connect to Wi-Fi")
                .setMessage("To use this app, connect to Wi-Fi and relaunch the app")
                .setPositiveButton("Wi-Fi settings", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Enable Wi-Fi and open Wi-Fi settings
                        openWifiSettings();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // You can add additional code here to handle the scenario where the user cancels.
                        // For simplicity, I'll just finish the activity.
                        finish();
                    }
                })
                .show();
    }

    private void openWifiSettings() {
        Intent wifiIntent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        startActivity(wifiIntent);
    }
}
