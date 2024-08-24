package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
import com.example.bookingapp.adapters.HostReservationListAdapter;
import com.example.bookingapp.adapters.ReportListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityAdministratorReportedUsersNavBarBinding;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.UserReportResponse;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdministratorReportedUsersActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ReportListAdapter.BlockUserListener {

    ActivityAdministratorReportedUsersNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ListView reportedUsersListView;
    private ReportListAdapter adapter;
    private List<UserReportResponse> reportedUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdministratorReportedUsersNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityAdministratorReportedUsers.toolbarAdministrator);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityAdministratorReportedUsers.toolbarAdministrator;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        reportedUsersListView = binding.activityAdministratorReportedUsers.activityAdministratorReportedUsersContent.reportedUsersListView;
        reportedUsers = new ArrayList<>();
        adapter = new ReportListAdapter(AdministratorReportedUsersActivity.this,reportedUsers,AdministratorReportedUsersActivity.this);
        reportedUsersListView.setAdapter(adapter);
        Call<Collection<UserReportResponse>> call = ClientUtils.userService.getAllReportedUsers(UserInfo.getToken());
        call.enqueue(new Callback<Collection<UserReportResponse>>() {
            @Override
            public void onResponse(Call<Collection<UserReportResponse>> call, Response<Collection<UserReportResponse>> response) {
                if(response.code() == 200){
                    reportedUsers.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
                else{
                    Toast.makeText(AdministratorReportedUsersActivity.this, "GETTING REPORTS SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Collection<UserReportResponse>> call, Throwable t) {
                Toast.makeText(AdministratorReportedUsersActivity.this, "GETTING REPORTS ERROR", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(AdministratorReportedUsersActivity.this, UpdateRequestsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.itemViewReviews){
            Intent intent = new Intent(AdministratorReportedUsersActivity.this, ReportedReviewsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.iconHome){
            return true;
        }
        else if(itemId == R.id.itemViewReportedUsers){
            Intent intent = new Intent(AdministratorReportedUsersActivity.this, AdministratorReportedUsersActivity.class);
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
                            Toast.makeText(AdministratorReportedUsersActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT);
                            startActivity(new Intent(AdministratorReportedUsersActivity.this,LoginActivity.class));
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
    public void onBlockUser(UUID userId) {
        Call<Boolean> call = ClientUtils.userService.blockUser(userId.toString(),UserInfo.getToken());
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if(response.code() == 200){
                    Toast.makeText(AdministratorReportedUsersActivity.this, "User blocked successfully", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(AdministratorReportedUsersActivity.this, "BLOCKING USER SERVER ERROR", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Toast.makeText(AdministratorReportedUsersActivity.this, "BLOCKING USER ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}