package com.example.bookingapp.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.databinding.ActivityAccommodationCreationBinding;
import com.example.bookingapp.databinding.ActivityAccommodationCreationMapBinding;
import com.example.bookingapp.entities.AccommodationPricelist;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class AccommodationCreationMapActivity extends AppCompatActivity {
    private ActivityAccommodationCreationMapBinding binding;
    MapView accCreationMapView;
    private FusedLocationProviderClient fusedLocationClient;
    private EditText accCreationNumber;
    private EditText accCreationStreet;
    private EditText accCreationCity;
    private EditText accCreationCountry;
    private Button backButton;
    private Button forwardButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Apartment name",AccommodationCreationActivity.newAccommodationRequest.getName());
        Log.e("Apartment price",String.valueOf(AccommodationCreationActivity.newAccommodationRequest.getPrice()));
        Log.e("Apartment amenity", AccommodationCreationActivity.newAccommodationRequest.getAmenities().get(0));
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_accommodation_creation_map);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivityAccommodationCreationMapBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        accCreationCity = binding.accCreationCity;
        accCreationNumber = binding.accCreationNumber;
        accCreationCountry = binding.accCreationCountry;
        accCreationStreet = binding.accCreationStreet;

        backButton = binding.creationPageReturnBack1;
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccommodationCreationMapActivity.this, AccommodationCreationActivity.class);
                startActivity(intent);
            }
        });
        forwardButton = binding.creationPageButton2;
        forwardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.example.bookingapp.entities.Location location = new com.example.bookingapp.entities.Location();
                location.setStreetNumber(15); // HARD CODED FOR NOW !!!!!!
                location.setCity(accCreationCity.getText().toString());
                location.setCountry(accCreationCountry.getText().toString());
                location.setStreet(accCreationStreet.getText().toString());
                AccommodationCreationActivity.newAccommodationRequest.setLocation(location);
                Intent intent = new Intent(AccommodationCreationMapActivity.this, AccommodationCreationDateActivity.class);
                startActivity(intent);
            }
        });
        if(AccommodationCreationActivity.isEdit){
            accCreationStreet.setText(AccommodationCreationActivity.newAccommodationRequest.getLocation().getStreet());
            accCreationNumber.setText(String.valueOf(AccommodationCreationActivity.newAccommodationRequest.getLocation().getStreetNumber()));
            accCreationCity.setText(AccommodationCreationActivity.newAccommodationRequest.getLocation().getCity());
            accCreationCountry.setText(AccommodationCreationActivity.newAccommodationRequest.getLocation().getCountry());
        }

        Configuration.getInstance().load(this, getPreferences(MODE_PRIVATE));
        accCreationMapView = findViewById(R.id.accCreationMapView);
        accCreationMapView.setMultiTouchControls(true);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationResult = fusedLocationClient.getLastLocation();
            locationResult.addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        GeoPoint currentLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
                        accCreationMapView.getController().setZoom(15);
                        accCreationMapView.getController().setCenter(currentLocation);
                    }
                }
            });
        }
        else{
            accCreationMapView.getController().setZoom(15);
            accCreationMapView.getController().setCenter(new GeoPoint(45.267136, 19.833549));
        }
        accCreationMapView.getOverlays().add(new MapEventsOverlay(new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                double latitude = p.getLatitude();
                double longitude = p.getLongitude();
                new AccommodationCreationMapActivity.ReverseGeocodingTask().execute(latitude,longitude);
                return true;
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        }));
    }
    private class ReverseGeocodingTask extends AsyncTask<Double, Void, String> {
        @Override
        protected String doInBackground(Double... params) {
            double latitude = params[0];
            double longitude = params[1];
            StringBuilder addressBuilder = new StringBuilder();
            JSONObject addressObject = null;
            try {
                String urlStr = "https://nominatim.openstreetmap.org/reverse?format=json&lat=" + latitude + "&lon=" + longitude + "&zoom=18&addressdetails=1";
                URL url = new URL(urlStr);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setReadTimeout(10000);
                connection.setConnectTimeout(15000);
                connection.setDoInput(true);
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                addressObject = jsonObject.getJSONObject("address");
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
            String houseNumber = addressObject.optString("house_number", "N/A");
            String road = addressObject.optString("road", "N/A");
            //String suburb = addressObject.optString("suburb", "N/A");
            String city = addressObject.optString("city", "N/A");
            //String state = addressObject.optString("state", "N/A");
            String postcode = addressObject.optString("postcode", "N/A");
            String country = addressObject.optString("country", "N/A");
            addressBuilder.append("House Number: ").append(houseNumber).append(",");
            addressBuilder.append("Road: ").append(road).append("\n");
            //addressBuilder.append("Suburb: ").append(suburb).append(",");
            addressBuilder.append("City: ").append(city).append(",");
            //addressBuilder.append("State: ").append(state).append("\n");
            addressBuilder.append("Postcode: ").append(postcode).append(",");
            addressBuilder.append("Country: ").append(country).append(",");
            return addressBuilder.toString();
        }

        @Override
        protected void onPostExecute(String address) {
            if (address != null) {
                Log.e("Adress", address);
                String[] addressParts = address.split(",");
                accCreationNumber.setText(addressParts[0]);
                accCreationStreet.setText(addressParts[1]);
                accCreationCity.setText(addressParts[2]);
                accCreationCountry.setText(addressParts[3]);
            } else {
                Toast.makeText(AccommodationCreationMapActivity.this, "Unable to get address", Toast.LENGTH_LONG).show();
            }
        }
    }
}