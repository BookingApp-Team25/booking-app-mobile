package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.bookingapp.R;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FavouriteAccommodationsListAdapter extends ArrayAdapter<AccommodationSummaryResponse> {
    private ArrayList<AccommodationSummaryResponse> accommodations;
    private RemoveFavouriteListener removeFavouriteListener;
    private AccommodationDetailsListener accommodationDetailsListener;

    public FavouriteAccommodationsListAdapter(Context context, ArrayList<AccommodationSummaryResponse> accommodations, RemoveFavouriteListener removeFavouriteListener, AccommodationDetailsListener accommodationDetailsListener) {
        super(context, R.layout.accommodation_favourite_card, accommodations);
        this.accommodations = accommodations;
        this.removeFavouriteListener = removeFavouriteListener;
        this.accommodationDetailsListener = accommodationDetailsListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        AccommodationSummaryResponse accommodation = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_favourite_card, parent, false);
        }
        LinearLayout productCard = convertView.findViewById(R.id.product_card_item);
        ImageView imageView = convertView.findViewById(R.id.product_image);
        TextView productTitle = convertView.findViewById(R.id.product_title);
        TextView productDescription = convertView.findViewById(R.id.product_description);
        TextView accommodationPrice = convertView.findViewById(R.id.accommodation_price);
        TextView accommodationRating = convertView.findViewById(R.id.accommodation_rating);
        TextView accommodationStatus = convertView.findViewById(R.id.accommodationStatusTextView);
        TextView accommodationEarliestDate = convertView.findViewById(R.id.accommodationHostEarliestDateTextView);
        FloatingActionButton removeFavouriteButton = convertView.findViewById(R.id.accommodationRemoveFavouriteButton);

        if (accommodation != null) {
            String photoUrl = accommodation.getPhoto(); // Replace this with your actual URL string
            // Load the image using Glide
            Glide.with(getContext())
                    .load(photoUrl)
                    .into(imageView);
            productTitle.setText(accommodation.getName()); // Adjust this part according to your AccommodationSummaryResponse class
            productDescription.setText(accommodation.getDescription()); // Adjust this part according to your AccommodationSummaryResponse class
            accommodationPrice.setText("price per night: " + accommodation.getPrice().toString() + "$");
            accommodationRating.setText(accommodation.getRating().toString() + "★");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDateString = accommodation.getEarliestAvailableDate().format(formatter);
            accommodationEarliestDate.setText(startDateString);

            accommodationStatus.setText(accommodation.getOnHoldStatus().toString());
            removeFavouriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(removeFavouriteListener != null){
                        removeFavouriteListener.onRemoveAccommodation(accommodation);
                    }
                }
            });
            productTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(accommodationDetailsListener !=null){
                        accommodationDetailsListener.onClickAccommodation(accommodation.getAccommodationId().toString());
                    }
                }
            });
        }

        return convertView;
    }
    public interface RemoveFavouriteListener {
        void onRemoveAccommodation(AccommodationSummaryResponse accommodationSummaryResponse);
    }
    public interface AccommodationDetailsListener {
        void onClickAccommodation(String accommodationId);
    }

}
