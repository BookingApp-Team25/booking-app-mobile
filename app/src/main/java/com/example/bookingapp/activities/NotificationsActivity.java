package com.example.bookingapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.NotificationListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityNotificationsBinding;
import com.example.bookingapp.databinding.ActivityNotificationsNavBarBinding;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationResponse;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, NotificationListAdapter.MarkNotificationListener {

    ActivityNotificationsNavBarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private NotificationListAdapter adapter;
    private ArrayList<NotificationResponse> notificationResponses;
    private ListView notificationsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationsNavBarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.activityNotifications.toolbarNotifications);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.activityNotifications.toolbarNotifications;
        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        notificationResponses = new ArrayList<>();
        adapter = new NotificationListAdapter(NotificationsActivity.this,notificationResponses,NotificationsActivity.this);
        notificationsListView = binding.activityNotifications.activityNotificationsContent.notificationsListView;
        notificationsListView.setAdapter(adapter);
        loadNotifications();


    }
    private void loadNotifications(){
        Call<Collection<NotificationResponse>> call = ClientUtils.notificationService.getAllNotifications(UserInfo.getUsername(),UserInfo.getToken());
        call.enqueue(new Callback<Collection<NotificationResponse>>() {
            @Override
            public void onResponse(Call<Collection<NotificationResponse>> call, Response<Collection<NotificationResponse>> response) {
                if(response.code() == 200){
                    notificationResponses.clear();
                    notificationResponses.addAll(response.body());
                    sortNotificationResponses();
                    adapter.notifyDataSetChanged();
                    Toast.makeText(NotificationsActivity.this, "Loaded notifications", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(NotificationsActivity.this, "server error loading notifications", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Collection<NotificationResponse>> call, Throwable t) {
                Toast.makeText(NotificationsActivity.this, "Error loading notifications", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_toolbar_host_main, menu); // DODATI DA ZA SVAKOG KORISNIKA BUDE RAZLICITO
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemCreateAccommodation) {
            Intent intent = new Intent(NotificationsActivity.this, AccommodationCreationActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.viewReservations){
            Intent intent = new Intent(NotificationsActivity.this, HostReservationsActivity.class);
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.viewProfile){
            Intent intent = new Intent(NotificationsActivity.this, ActivityHostProfile.class);
            intent.putExtra("hostUsername", "d: " + UserInfo.getUsername());
            startActivity(intent);
            return true;
        }
        else if(itemId == R.id.iconHome){
            Intent intent = new Intent(NotificationsActivity.this, HostMainActivity.class);
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
    private void sortNotificationResponses(){
        Collections.sort(notificationResponses, new Comparator<NotificationResponse>() {
            @Override
            public int compare(NotificationResponse e1, NotificationResponse e2) {
                return e2.getSendTime().compareTo(e1.getSendTime());
            }
        });
    }

    @Override
    public void onMarkNotification(UUID notificationId) {
        Call<MessageResponse> call = ClientUtils.notificationService.seenNotification(notificationId,UserInfo.getToken());
        call.enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if(response.code() == 200){
                    Toast.makeText(NotificationsActivity.this, "Marked as seen", Toast.LENGTH_SHORT).show();
                    loadNotifications();
                }
                else{
                    Toast.makeText(NotificationsActivity.this, "SERVER ERROR SEEN", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                Toast.makeText(NotificationsActivity.this, "ERROR SEEN", Toast.LENGTH_SHORT).show();
            }
        });
    }
}