package com.example.bookingapp.activities;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.UpdateRequestListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityHostMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityUpdateRequestsBinding;
import com.example.bookingapp.dto.AccommodationUpdateSummaryResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.security.UserInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateRequestsActivity extends AppCompatActivity implements UpdateRequestListAdapter.RequestAcceptListener, UpdateRequestListAdapter.RequestDenyListener {
    private UpdateRequestListAdapter adapter;
    private ListView updateRequestsListView;
    private ActivityUpdateRequestsBinding binding;
    private List<AccommodationUpdateSummaryResponse> updateRequests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityUpdateRequestsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateRequests = new ArrayList<>();
        loadUpdateRequests();
        updateRequestsListView = binding.updateRequestsListView;
        adapter = new UpdateRequestListAdapter(UpdateRequestsActivity.this,updateRequests,UpdateRequestsActivity.this,UpdateRequestsActivity.this);
        updateRequestsListView.setAdapter(adapter);
    }
    private void loadUpdateRequests(){
        Call<Collection<AccommodationUpdateSummaryResponse>> call = ClientUtils.accommodationUpdateService.getAllUpdates(UserInfo.getToken());
        call.enqueue(new Callback<Collection<AccommodationUpdateSummaryResponse>>() {
            @Override
            public void onResponse(Call<Collection<AccommodationUpdateSummaryResponse>> call, Response<Collection<AccommodationUpdateSummaryResponse>> response) {
                if(response.code() == 200){
                    Toast.makeText(UpdateRequestsActivity.this, "ADDED UPDATES", Toast.LENGTH_SHORT).show();
                    updateRequests.clear();
                    updateRequests.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(UpdateRequestsActivity.this, "ERROR LOADING UPDATES", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Collection<AccommodationUpdateSummaryResponse>> call, Throwable t) {
                Toast.makeText(UpdateRequestsActivity.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onAcceptRequest(UUID updateRequestId) {
        Call<MessageResponse> call = ClientUtils.accommodationUpdateService.resolveAccommodationUpdate(updateRequestId,1,UserInfo.getToken());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
               if(response.code()==200){
                   Toast.makeText(UpdateRequestsActivity.this, "UPDATE APPROVED", Toast.LENGTH_SHORT).show();

               }
               else{
                   Toast.makeText(UpdateRequestsActivity.this, "SERVER ERROR", Toast.LENGTH_SHORT).show();
               }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(UpdateRequestsActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDenyRequest(UUID updateRequestId) {
        Toast.makeText(UpdateRequestsActivity.this, "UPDATE DENIED", Toast.LENGTH_SHORT).show();
    }
}