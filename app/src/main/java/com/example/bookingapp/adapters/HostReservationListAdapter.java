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
import com.example.bookingapp.dto.HostReservationResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class HostReservationListAdapter extends ArrayAdapter<HostReservationResponse> {
    List<HostReservationResponse> hostReservationResponses;

    public HostReservationListAdapter(Context context, List<HostReservationResponse> hostReservationResponses){
        super(context, R.layout.host_reservation_card, hostReservationResponses);
        this.hostReservationResponses = hostReservationResponses;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        HostReservationResponse hostReservationResponse = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.host_reservation_card, parent, false);
        }


        ConstraintLayout hostResrevationCard = convertView.findViewById(R.id.hostReservationCard);
        ImageView imageView = convertView.findViewById(R.id.hostReservationImageView);
        TextView accommodationName = convertView.findViewById(R.id.hostReservationApartmentNameTextView);
        TextView guestName = convertView.findViewById(R.id.hostReservationGuestNameTextView);
        TextView datePeriod = convertView.findViewById(R.id.hostReservationDatePeriodTextView);
        TextView reservationStatus = convertView.findViewById(R.id.hostReservationStatusTextView);
        if (hostReservationResponse != null) {
            String photoUrl = hostReservationResponse.getAccommodationPhoto(); // Replace this with your actual URL string
            // Load the image using Glide
            Glide.with(getContext())
                    .load(photoUrl)
                    .into(imageView);
            accommodationName.setText(hostReservationResponse.getAccommodationName());
            guestName.setText(hostReservationResponse.getGuestName());
            reservationStatus.setText(hostReservationResponse.getReservationStatus().toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDateString = hostReservationResponse.getReservedDate().getStartDate().format(formatter);
            String endDateString = hostReservationResponse.getReservedDate().getEndDate().format(formatter);
            datePeriod.setText(startDateString + " : " + endDateString);
        }

        return convertView;
    }
}
