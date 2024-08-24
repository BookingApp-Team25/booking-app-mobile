package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
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
import com.example.bookingapp.adapters.ReportListAdapter;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityReportedReviewsBinding;
import com.example.bookingapp.databinding.ActivityReportedReviewsNavbarBinding;
import com.example.bookingapp.dto.ReviewResponse;
import com.example.bookingapp.dto.enums.ReviewType;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReportedReviewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReviewListAdapter.ReviewReportListener, ReviewListAdapter.ReviewDeleteListener {

    ActivityReportedReviewsNavbarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ListView reportedReviewsViewList;
    private ReviewListAdapter adapter;
    private ArrayList<ReviewResponse> reportedReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReportedReviewsNavbarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityReportedReviews.toolbarAdministrator);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityReportedReviews.toolbarAdministrator;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        reportedReviewsViewList = binding.activityReportedReviews.activityReportedReviewsContent.reportedReviewsListView;
        reportedReviews = new ArrayList<>();
        adapter = new ReviewListAdapter(ReportedReviewsActivity.this,reportedReviews,ReportedReviewsActivity.this,ReportedReviewsActivity.this,"");
        reportedReviewsViewList.setAdapter(adapter);
        Call<Collection<ReviewResponse>> call = ClientUtils.reviewService.getAllReportedReviews(UserInfo.getToken());
        call.enqueue(new Callback<Collection<ReviewResponse>>() {
            @Override
            public void onResponse(Call<Collection<ReviewResponse>> call, Response<Collection<ReviewResponse>> response) {
                if(response.code() == 200){
                    reportedReviews.clear();
                    reportedReviews.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ReportedReviewsActivity.this, "REPORTED REVIEWS SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Collection<ReviewResponse>> call, Throwable t) {
                Toast.makeText(ReportedReviewsActivity.this, "REPORTED REVIES ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_administrator_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemViewRequests) {
            Intent intent = new Intent(ReportedReviewsActivity.this, UpdateRequestsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.itemViewRequests){
            Intent intent = new Intent(ReportedReviewsActivity.this, ReportedReviewsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.iconHome){
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

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onDeleteReview(ReviewResponse review) {
        boolean flag = false;
        if(review.getType() == ReviewType.AccommodationReview){
            flag = true;
        }
        Call<Boolean> call = ClientUtils.reviewService.deleteReview(review.getId().toString(),flag,UserInfo.getToken());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200){
                    Toast.makeText(ReportedReviewsActivity.this, "Successfully deleted review", Toast.LENGTH_SHORT).show();
//                    Intent intent = getIntent();
//                    finish();
//                    startActivity(intent);
                    reportedReviews.removeIf(reportedReview -> reportedReview.getId().equals(review.getId()));
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(ReportedReviewsActivity.this, "DELETE REVIEW SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(ReportedReviewsActivity.this, "DELETE REIVEW ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onReportReview(ReviewResponse review) {
    }
}