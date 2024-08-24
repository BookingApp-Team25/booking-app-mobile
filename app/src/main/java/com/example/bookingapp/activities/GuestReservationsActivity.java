package com.example.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityGuestReservationsBinding;
import com.example.bookingapp.databinding.ActivityGuestReservationsNavBarBinding;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.fragments.GuestFavouriteAccommodationsFragment;
import com.example.bookingapp.fragments.GuestReservationsFragment;
import com.example.bookingapp.fragments.HostReservationRequestsFragment;
import com.example.bookingapp.fragments.HostReservationsFragment;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GuestReservationsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    ActivityGuestReservationsNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private Button reservationsButton;
    private Button favoureAccommodationsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGuestReservationsNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityGuestReservations.toolbarGuest);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityGuestReservations.toolbarGuest;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        reservationsButton = binding.activityGuestReservations.activityGuestReservationsContent.guestReservationsButton;
        reservationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment();
            }
        });
        favoureAccommodationsButton = binding.activityGuestReservations.activityGuestReservationsContent.guestFavouriteAccommodationsButton;
        favoureAccommodationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchFragment();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_guest_main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if(itemId == R.id.itemNotificationsView){
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

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_logout:
                Call<MessageResponse> call= ClientUtils.authService.logout(UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        if(response.isSuccessful()){
                            Toast.makeText(GuestReservationsActivity.this,response.body().getMessage(),Toast.LENGTH_SHORT);
                            startActivity(new Intent(GuestReservationsActivity.this,LoginActivity.class));
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
                break;
            case R.id.nav_account:
                startActivity(new Intent(this,AccountActivity.class));
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    public void switchFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Fragment currentFragment = fragmentManager.findFragmentById(R.id.guestReservationsFragmentContainer);

        if (currentFragment instanceof GuestReservationsFragment) {
            fragmentTransaction.replace(R.id.guestReservationsFragmentContainer, new GuestFavouriteAccommodationsFragment());
        } else {
            fragmentTransaction.replace(R.id.guestReservationsFragmentContainer, new GuestReservationsFragment());
        }
        fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }
}