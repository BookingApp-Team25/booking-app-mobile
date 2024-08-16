package com.example.bookingapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.viewpager2.widget.ViewPager2;

import com.example.bookingapp.R;
import com.example.bookingapp.adapters.ImagePagerAdapter;
import com.example.bookingapp.adapters.NotificationHelper;
import com.example.bookingapp.adapters.ReviewListAdapter;
import com.example.bookingapp.clients.ClientUtils;
import com.example.bookingapp.databinding.ActivityAccommodationsBinding;
import com.example.bookingapp.dto.AccommodationDetailsResponse;
import com.example.bookingapp.dto.MessageResponse;
import com.example.bookingapp.dto.NotificationRequest;
import com.example.bookingapp.dto.ReviewRequest;
import com.example.bookingapp.dto.ReviewResponse;
import com.example.bookingapp.dto.enums.ReviewType;
import com.example.bookingapp.network.RetrofitClient;
import com.example.bookingapp.clients.AccommodationService;
import com.example.bookingapp.security.UserInfo;
import com.google.android.material.navigation.NavigationView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.view.View;
public class AccommodationsActivity extends AppCompatActivity implements ReviewListAdapter.ReviewDeleteListener,ReviewListAdapter.ReviewReportListener , NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "AccommodationsActivity";
    private ListView listViewReviews;
    private ReviewListAdapter reviewListAdapter;
    private ArrayList<ReviewResponse> reviewList = new ArrayList<>();
    private AppBarConfiguration appBarConfiguration;
    private ActivityAccommodationsBinding binding;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawer;
    private androidx.appcompat.widget.Toolbar toolbar;
    private NavigationView navigationView;
    private ViewPager2 viewPager;
    private EditText reviewText;
    private MapView mapView; // Add MapView
    private TextView ownerText;
    private AccommodationDetailsResponse accommodation;
    private Button sendButton;
    private UUID accommodationId;
    private UUID hostId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Initialize binding
        binding = ActivityAccommodationsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views

        setSupportActionBar(binding.drawerLayout.activityGuestMainNoContent.toolbar);
        ActionBar actionBar = getSupportActionBar();
        RatingBar reviewRating = findViewById(R.id.review_rating);
        reviewRating.setRating(1);
        reviewText = findViewById(R.id.review_text);
        sendButton = findViewById(R.id.send_review);
        ownerText = findViewById(R.id.owner);
        ownerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AccommodationsActivity.this, ActivityHostProfile.class);
                intent.putExtra("hostUsername",ownerText.getText());
                startActivity(intent);
            }
        });

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setHomeButtonEnabled(true);
        }
        drawer = binding.drawerLayout.drawerLayout;
        navigationView = binding.drawerLayout.navView;
        navigationView.setNavigationItemSelectedListener(this);
        toolbar = binding.drawerLayout.activityGuestMainNoContent.toolbar;

        actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);

        actionBarDrawerToggle.syncState();

        // Retrieve the accommodationId from the Intent
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("accommodationId")) {
            accommodationId = (UUID) intent.getSerializableExtra("accommodationId");
            Log.d(TAG, "Accommodation ID: " + accommodationId);
            fetchAccommodationDetails(accommodationId);
        } else {
            Log.d(TAG, "No accommodationId found in the intent.");
        }

        if(UserInfo.getRole().equals("ROLE_Guest")){
            sendButton.setVisibility(View.VISIBLE);
            reviewText.setVisibility(View.VISIBLE);
            reviewRating.setVisibility(View.VISIBLE);

        } else {
            sendButton.setVisibility(View.GONE);
            reviewText.setVisibility(View.GONE);
            reviewRating.setVisibility(View.GONE);
        }

        // Pressing the button triggers the ReservationActivity
        Button reserveButton = findViewById(R.id.make_reserbation_button);
        reserveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (accommodation != null) {
                    // Navigate to the Reservation Activity
                    Intent intent = new Intent(AccommodationsActivity.this, ReservationActivity.class);

                    int minGuests = accommodation.getMinGuests();
                    int maxGuests = accommodation.getMaxGuests();

                    intent.putParcelableArrayListExtra("availability", new ArrayList<>(accommodation.getAvailability()));
                    intent.putExtra("minGuests", minGuests);
                    intent.putExtra("maxGuests", maxGuests);
                    intent.putExtra("hostId", hostId.toString());
                    intent.putExtra("accommodationId", accommodationId.toString());

                    startActivity(intent);
                } else {
                    Log.e(TAG, "Accommodation details are not available yet");
                }
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = reviewRating.getRating();
                String review = reviewText.getText().toString();
                ReviewRequest reviewRequest = new ReviewRequest(review,rating,UserInfo.getUsername(),accommodationId.toString(), ReviewType.AccommodationReview);
                Call<MessageResponse> call=ClientUtils.reviewService.createReview(reviewRequest,UserInfo.getToken());
                call.enqueue(new Callback<MessageResponse>() {
                    @Override
                    public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                        Toast.makeText(AccommodationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        reviewText.setText("");
                    }

                    @Override
                    public void onFailure(Call<MessageResponse> call, Throwable t) {

                    }
                });
                NotificationRequest notificationRequest = new NotificationRequest(accommodation.getHostUsername(),"New accommodation review,Korisnik " + UserInfo.getUsername() + " je komentarisao vas smestaj", LocalDateTime.now(),false);
                NotificationHelper.createAndSaveNotification(AccommodationsActivity.this,notificationRequest);
            }
        });
    }


    private void fetchAccommodationDetails(UUID accommodationId) {
        AccommodationService apiService = ClientUtils.accommodationService;
        Call<AccommodationDetailsResponse> call = apiService.getAccommodation(accommodationId);

        call.enqueue(new Callback<AccommodationDetailsResponse>() {
            @Override
            public void onResponse(Call<AccommodationDetailsResponse> call, Response<AccommodationDetailsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Accommodation details retrieved: " + response.body());
                    accommodation = new AccommodationDetailsResponse(response.body());
                    hostId = (UUID) response.body().getHostId();
                    populateUI(accommodation);
                    fetchReviews(accommodationId);
                    ownerText.setText("Owner: "+accommodation.getHostUsername());
                } else {
                    Log.d(TAG, "Response received but not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<AccommodationDetailsResponse> call, Throwable t) {
                Log.e(TAG, "API call failed: ", t);
            }
        });
    }



    private void fetchReviews(UUID accommodationId) {
        Call<List<ReviewResponse>> call = ClientUtils.reviewService.getAllReviews(accommodationId.toString(),true);

        call.enqueue(new Callback<List<ReviewResponse>>() {
            @Override
            public void onResponse(Call<List<ReviewResponse>> call, Response<List<ReviewResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    reviewList.clear();
                    reviewList.addAll(response.body());
                    reviewListAdapter = new ReviewListAdapter(AccommodationsActivity.this, reviewList, AccommodationsActivity.this, AccommodationsActivity.this, accommodation.getHostUsername());
                    listViewReviews = findViewById(R.id.listViewReviews);
                    listViewReviews.setAdapter(reviewListAdapter);
                    reviewListAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnChildren(listViewReviews);
                } else {
                    Log.d("AccommodationsActivity", "Response received but not successful: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ReviewResponse>> call, Throwable t) {
                Log.e("AccommodationsActivity", "API call failed: ", t);
            }
        });
    }

    private void populateUI(AccommodationDetailsResponse accommodation) {
        TextView nameTextView = findViewById(R.id.textView6);
        TextView descriptionTextView = findViewById(R.id.textView9);
        TextView amenitiesTextView = findViewById(R.id.textViewAmenities);
        RatingBar ratingTextView = findViewById(R.id.ratingBar);
        viewPager = findViewById(R.id.viewPager);

        nameTextView.setText(accommodation.getName());
        descriptionTextView.setText(accommodation.getDescription());
        String amenitiesString = "This accommodation includes:\n\t\t\t";
        for (String amenity : accommodation.getAmenities()) {
            amenitiesString += amenity + ", ";
        }
        amenitiesTextView.setText(amenitiesString.substring(0, amenitiesString.length() - 2));
        ratingTextView.setRating((float) accommodation.getRating());

        // Set up ViewPager with images
        List<String> imageUrls = accommodation.getPhotos();
        ImagePagerAdapter adapter = new ImagePagerAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);

        // Add MapView
        mapView = findViewById(R.id.mapView);
        mapView.getController().setZoom(15);
        mapView.getController().setCenter(new GeoPoint(45.267136, 19.833549));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_guest_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.itemNotificationsView) {
            Intent intent = new Intent(this, NotificationsActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.itemReservationView) {
            Intent intent = new Intent(this, GuestReservationsActivity.class);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onDeleteReview(ReviewResponse review) {
        ClientUtils.reviewService.deleteReview(review.getId().toString(), true, UserInfo.getToken()).enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful()) {
                    // If the review is successfully deleted from the server, remove it from the list
                    reviewList.remove(review);
                    reviewListAdapter.notifyDataSetChanged();
                } else {
                    Log.e(TAG, "Failed to delete review: " + response.message());
                    // You can show an error message to the user here if needed
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e(TAG, "Failed to delete review: " + t.getMessage());
                // You can show an error message to the user here if needed
            }
        });
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(
                    View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            );
            totalHeight += listItem.getMeasuredHeight() + 250;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onReportReview(ReviewResponse review) {
        ClientUtils.reviewService.reportReview(review.getId().toString(), true, UserInfo.getToken()).enqueue(new Callback<MessageResponse>() {
            @Override
            public void onResponse(Call<MessageResponse> call, Response<MessageResponse> response) {
                if (response.isSuccessful()) {
                    // If the review is successfully reported, show a Toast with the success message
                    Toast.makeText(AccommodationsActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    review.setReported(true);
                    reviewListAdapter.notifyDataSetChanged();
                } else {
                    // Show a Toast with the error message
                    Toast.makeText(AccommodationsActivity.this, "Failed to report review: " + response.message(), Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Failed to report review: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MessageResponse> call, Throwable t) {
                // Show a Toast with the failure message
                Toast.makeText(AccommodationsActivity.this, "Failed to report review: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e(TAG, "Failed to report review: " + t.getMessage());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }
}
