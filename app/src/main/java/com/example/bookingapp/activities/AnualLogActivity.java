package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.MonthlyLogAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAnualLogBinding;
import com.example.bookingapp.dto.AccommodationMonthlyLog;
import com.example.bookingapp.dto.AccommodationMonthlyLogCollection;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnualLogActivity extends AppCompatActivity {
    private ActivityAnualLogBinding binding;
    private TextView accommodationNameTextView;
    private ListView logsListView;
    private UUID accommodationId;
    private List<AccommodationMonthlyLog> months;
    private MonthlyLogAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAnualLogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        accommodationNameTextView = binding.AnualLogAccommodationNameTextView;
        logsListView = binding.AnualLogListView;
        Intent intent = getIntent();
        if(intent != null){
            accommodationNameTextView.setText(intent.getStringExtra("accommodationName"));
            accommodationId = UUID.fromString(intent.getStringExtra("accommodationId"));
        }
        months = new ArrayList<>();
        adapter = new MonthlyLogAdapter(AnualLogActivity.this,months);
        logsListView.setAdapter(adapter);
        Call<AccommodationMonthlyLogCollection> call = ClientUtils.accommodationService.generateAnnualLog(accommodationId, UserInfo.getToken());
        call.enqueue(new Callback<AccommodationMonthlyLogCollection>() {
            @Override
            public void onResponse(Call<AccommodationMonthlyLogCollection> call, Response<AccommodationMonthlyLogCollection> response) {
                if(response.code() == 200){
                    months.addAll(response.body().getMonths());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(AnualLogActivity.this, "GENERATE LOGS SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccommodationMonthlyLogCollection> call, Throwable t) {
                Toast.makeText(AnualLogActivity.this, "GENERATE LOGS  ERROR", Toast.LENGTH_SHORT).show();
            }
        });



    }
}