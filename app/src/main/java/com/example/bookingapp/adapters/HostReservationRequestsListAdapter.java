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
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.HostReservationResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class HostReservationRequestsListAdapter extends ArrayAdapter<HostReservationResponse> {
    List<HostReservationResponse> hostReservationResponses;
    AcceptReservationRequestListener acceptReservationRequestListener;
    RejectReservationRequestListener rejectReservationRequestListener;

    public HostReservationRequestsListAdapter(Context context, List<HostReservationResponse> hostReservationResponses, AcceptReservationRequestListener acceptReservationRequestListener, RejectReservationRequestListener rejectReservationRequestListener){
        super(context, R.layout.host_reservation_request_card, hostReservationResponses);
        this.hostReservationResponses = hostReservationResponses;
        this.acceptReservationRequestListener = acceptReservationRequestListener;
        this.rejectReservationRequestListener = rejectReservationRequestListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        HostReservationResponse hostReservationResponse = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.host_reservation_request_card, parent, false);
        }


        ConstraintLayout hostResrevationCard = convertView.findViewById(R.id.hostReservationRequestCard);
        ImageView imageView = convertView.findViewById(R.id.hostReservationRequestImageView);
        TextView accommodationName = convertView.findViewById(R.id.hostReservationRequestApartmentNameTextView);
        TextView guestName = convertView.findViewById(R.id.hostReservationRequestGuestNameTextView);
        TextView datePeriod = convertView.findViewById(R.id.hostReservationRequestDatePeriodTextView);
        FloatingActionButton acceptButton = convertView.findViewById(R.id.acceptReservationRequestFloatingButotn);
        FloatingActionButton rejectButton = convertView.findViewById(R.id.rejectReservationRequestFloatingButton);

        if (hostReservationResponse != null) {
            String photoUrl = hostReservationResponse.getAccommodationPhoto(); // Replace this with your actual URL string
            // Load the image using Glide
            Glide.with(getContext())
                    .load(photoUrl)
                    .into(imageView);
            accommodationName.setText(hostReservationResponse.getAccommodationName());
            guestName.setText(hostReservationResponse.getGuestName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDateString = hostReservationResponse.getReservedDate().getStartDate().format(formatter);
            String endDateString = hostReservationResponse.getReservedDate().getEndDate().format(formatter);
            datePeriod.setText(startDateString + " : " + endDateString);
            acceptButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(acceptReservationRequestListener != null){
                        acceptReservationRequestListener.onAcceptReservationRequest(hostReservationResponse.getReservationId());
                    }
                }
            });
            rejectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(rejectReservationRequestListener != null){
                        rejectReservationRequestListener.onRejectReservationRequest(hostReservationResponse.getReservationId());
                    }
                }
            });
        }

        return convertView;
    }
    public interface AcceptReservationRequestListener {
        void onAcceptReservationRequest(UUID reservationId);
    }
    public interface RejectReservationRequestListener {
        void onRejectReservationRequest(UUID reservationId);
    }
}
