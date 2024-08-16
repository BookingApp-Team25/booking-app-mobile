package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.NotificationHelper;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityHostMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityHostProfileBinding;
import com.example.bookingapp.databinding.ActivityHostProfileNavBarBinding;
import com.example.bookingapp.dto.AccountDetailsResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationRequest;
import com.example.bookingapp.dto.ReviewRequest;
import com.example.bookingapp.dto.ReviewResponse;
import com.example.bookingapp.dto.enums.ReviewType;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActivityHostProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReviewListAdapter.ReviewDeleteListener, ReviewListAdapter.ReviewReportListener {
    ActivityHostProfileNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ArrayList<ReviewResponse> hostReviews;
    private String hostId;
    private String hostUsername;
    private ReviewListAdapter adapter;
    private ListView reviewListView;
    private Button hostReviewConfirmButton;
    private EditText hostReviewInputText;
    private RatingBar hostReviewRating;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostProfileNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityHostProfile.toolbarHostProfile);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityHostProfile.toolbarHostProfile;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        hostReviewRating = binding.activityHostProfile.activityHostProfileContent.hostReviewRatingBar;
        hostReviewConfirmButton = binding.activityHostProfile.activityHostProfileContent.hostReviewConfirmButton;
        hostReviewInputText = binding.activityHostProfile.activityHostProfileContent.hostReviewInputText;


        hostReviews = new ArrayList<>();
        reviewListView = binding.activityHostProfile.activityHostProfileContent.hostProfileReviewsListView;
        Intent intent = getIntent();
        hostUsername = intent.getStringExtra("hostUsername");
        hostUsername = hostUsername.split(":")[1];
        hostUsername = hostUsername.trim();
        adapter = new ReviewListAdapter(ActivityHostProfile.this,hostReviews,ActivityHostProfile.this,ActivityHostProfile.this,hostUsername);
        reviewListView.setAdapter(adapter);

        hostReviewConfirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ReviewRequest reviewRequest = new ReviewRequest(hostReviewInputText.getText().toString(),hostReviewRating.getRating(),UserInfo.getUsername(),hostUsername, ReviewType.HostReview);
                Call<MessageResponse> call = ClientUtils.reviewService.createReview(reviewRequest,UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if(response.code()==200){
                            Toast.makeText(ActivityHostProfile.this, "Kreiran komentar", Toast.LENGTH_SHORT).show();
                            loadReviews();
                            NotificationRequest notificationRequest = new NotificationRequest(hostUsername,"New profile review,Korisnik " + UserInfo.getUsername() + " je komentarisao vas profil", LocalDateTime.now(),false);
                            NotificationHelper.createAndSaveNotification(ActivityHostProfile.this,notificationRequest);
                        }
                        else{
                            Toast.makeText(ActivityHostProfile.this, "SERVER ERROR add review", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {
                        Toast.makeText(ActivityHostProfile.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        String guestUsername = UserInfo.getUsername();
        Call<AccountDetailsResponse> call = ClientUtils.userService.getAccountDetails(hostUsername, UserInfo.getToken());
        call.enqueue(new Callback<AccountDetailsResponse>() {
            @Override
            public void onResponse(Call<AccountDetailsResponse> call, Response<AccountDetailsResponse> response) {
                if(response.code()==200){
                    hostId = response.body().getId();
                    loadReviews();
                }
                else{
                    Toast.makeText(ActivityHostProfile.this, "SERVER ERROR getting id", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<AccountDetailsResponse> call, Throwable t) {
                Toast.makeText(ActivityHostProfile.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
            }
        });


    }
    private void loadReviews(){
        Call<List<ReviewResponse>> call = ClientUtils.reviewService.getAllReviews(hostId,false);
        call.enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                if(response.code()==200){
                    hostReviews.clear();
                    hostReviews.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ActivityHostProfile.this, "SERVER ERROR loading reviews", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {
                Toast.makeText(ActivityHostProfile.this, "FAILURE ERROR", Toast.LENGTH_SHORT).show();
            }
        });
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
        if (itemId == R.id.itemCreateAccommodation) {
            Intent intent = new Intent(ActivityHostProfile.this, AccommodationCreationActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.iconHome){
            Intent intent = new Intent(ActivityHostProfile.this, HostMainActivity.class);
            startActivity(intent);
            return true;
        }
        else{
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_account:
                Intent intent = new Intent(this,  AccountActivity.class);
                startActivity(intent);
                return true;
            case R.id.nav_logout:
                Call<MessageResponse> call= ClientUtils.authService.logout(UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(ActivityHostProfile.this,response.body().getMessage(),Toast.LENGTH_SHORT);
                            startActivity(new Intent(ActivityHostProfile.this,LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDeleteReview(ReviewResponse review) {

    }

    @Override
    public void onReportReview(ReviewResponse review) {
        ClientUtils.reviewService.reportReview(review.getId().toString(), false, UserInfo.getToken()).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    // If the review is successfully reported, show a Toast with the success message
                    Toast.makeText(ActivityHostProfile.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    review.setReported(true);
                    adapter.notifyDataSetChanged();
                } else {
                    // Show a Toast with the error message
                    Toast.makeText(ActivityHostProfile.this, "Failed to report review: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                // Show a Toast with the failure message
                Toast.makeText(ActivityHostProfile.this, "Failed to report review: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}