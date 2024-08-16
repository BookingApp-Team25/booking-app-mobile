package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
import com.example.bookingapp.databinding.ActivityAdministratorMainNavBarBinding;
import com.example.bookingapp.databinding.ActivityReportedReviewsBinding;
import com.example.bookingapp.databinding.ActivityReportedReviewsNavbarBinding;
import com.google.android.material.navigation.NavigationView;

public class ReportedReviewsActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityReportedReviewsNavbarBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;

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
}