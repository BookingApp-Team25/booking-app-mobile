package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.bookingapp.R;
import com.example.bookingapp.dto.AccommodationUpdateSummaryResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

public class UpdateRequestListAdapter extends ArrayAdapter<AccommodationUpdateSummaryResponse> {

    List<AccommodationUpdateSummaryResponse> uUpdateRequests;
    RequestAcceptListener requestAcceptListener;
    RequestDenyListener requestDenyListener;

    public UpdateRequestListAdapter(Context context, List<AccommodationUpdateSummaryResponse> updateRequests, RequestAcceptListener requestAcceptListener, RequestDenyListener requestDenyListener){
        super(context, R.layout.accommodation_update_card, updateRequests);
        uUpdateRequests = updateRequests;
        this.requestDenyListener = requestDenyListener;
        this.requestAcceptListener = requestAcceptListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AccommodationUpdateSummaryResponse updateRequest = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_update_card, parent, false);
        }

        ConstraintLayout updateCard = convertView.findViewById(R.id.accommodationUpdateCard);
        ImageView imageView = convertView.findViewById(R.id.accommodationUpdateImageView);
        TextView accommodationName = convertView.findViewById(R.id.accommodationUpdateNameTextView);
        TextView accommodationDescription = convertView.findViewById(R.id.accommodationUpdateDescriptionTextView);
        TextView accommodationType = convertView.findViewById(R.id.accommodationUpdateTypeTextView);
        FloatingActionButton acceptButton = convertView.findViewById(R.id.updateRequestConfirmFloatingButton);
        FloatingActionButton denyButton = convertView.findViewById(R.id.updateRequestDenyFloatingButton);
        if (updateRequest != null) {
            String photoUrl = updateRequest.getAccommodationPhoto(); // Replace this with your actual URL string
            // Load the image using Glide
            Glide.with(getContext())
                    .load(photoUrl)
                    .into(imageView);
            accommodationName.setText(updateRequest.getAccommodationName());
            accommodationDescription.setText(updateRequest.getAccommodationDescription());
            accommodationType.setText(updateRequest.getType().toString());
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(requestAcceptListener != null){
                        requestAcceptListener.onAcceptRequest(updateRequest.getId());
                        acceptButton.setVisibility(View.GONE);
                        denyButton.setVisibility(View.GONE);
                    }
                }
            });
            denyButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(requestDenyListener != null){
                        requestDenyListener.onDenyRequest(updateRequest.getId());
                        acceptButton.setVisibility(View.GONE);
                        denyButton.setVisibility(View.GONE);
                    }
                }
            });
        }

        return convertView;
    }


    public interface RequestAcceptListener {
        void onAcceptRequest(UUID updateRequestId);
    }

    public interface RequestDenyListener {
        void onDenyRequest(UUID updateRequestId);
    }



}
