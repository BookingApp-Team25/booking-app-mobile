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
import com.example.bookingapp.dto.ReservationRequest;
import com.example.bookingapp.dto.enums.ReservationStatus;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class GuestReservationAdapter extends ArrayAdapter<ReservationRequest> {
    List<ReservationRequest> guestReservationRequests;
    CancelReservationListener cancelReservationListener;


    public GuestReservationAdapter(Context context, List<ReservationRequest> guestReservationRequests, CancelReservationListener cancelReservationListener){
        super(context, R.layout.guest_reservation_card, guestReservationRequests);
        this.guestReservationRequests = guestReservationRequests;
        this.cancelReservationListener = cancelReservationListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        ReservationRequest guestReservationRequest = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.guest_reservation_card, parent, false);
        }


        ConstraintLayout hostResrevationCard = convertView.findViewById(R.id.guestReservationCard);
        TextView reservationIdTextView = convertView.findViewById(R.id.guestReservationIdTextView);
        TextView reservationDatePeriodTextView = convertView.findViewById(R.id.guestReservationDatePeriod);
        TextView reservationPriceTextView = convertView.findViewById(R.id.guestReservationPrice);
        TextView reservationStatus = convertView.findViewById(R.id.guestReservationStatus);
        FloatingActionButton guestCancelReservationButton = convertView.findViewById(R.id.guestReservationCancelButton);
        if (guestReservationRequest != null) {
            reservationIdTextView.setText(guestReservationRequest.getReservationId().toString());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDateString = guestReservationRequest.getReservedDate().getStartDate().format(formatter);
            String endDateString = guestReservationRequest.getReservedDate().getEndDate().format(formatter);
            reservationDatePeriodTextView.setText(startDateString + " : " + endDateString);
            reservationPriceTextView.setText(String.valueOf(guestReservationRequest.getPrice()));
            reservationStatus.setText(guestReservationRequest.getReservationStatus().toString());
            if(!(guestReservationRequest.getReservationStatus() == ReservationStatus.ACCEPTED || guestReservationRequest.getReservationStatus() == ReservationStatus.WAITING_FOR_APPROVAL)){
                guestCancelReservationButton.setVisibility(View.GONE);
            }
            guestCancelReservationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(cancelReservationListener != null){
                        cancelReservationListener.onCancel(guestReservationRequest.getReservationId().toString());
                    }
                }
            });

        }

        return convertView;
    }
    public interface CancelReservationListener {
        void onCancel(String reservationId);
    }
}
