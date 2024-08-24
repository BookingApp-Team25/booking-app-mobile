package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.bookingapp.R;
import com.example.bookingapp.dto.AccommodationLog;
import com.example.bookingapp.dto.AccommodationSummaryResponse;
import com.example.bookingapp.dto.HostReservationResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class AccommodationLogAdapter extends ArrayAdapter<AccommodationLog> {
    List<AccommodationLog> accommodationLogs;
    AnnualLogListener annualLogListener;

    public AccommodationLogAdapter(Context context, List<AccommodationLog> accommodationLogs, AnnualLogListener annualLogListener){
        super(context, R.layout.host_reservation_card, accommodationLogs);
        this.accommodationLogs = accommodationLogs;
        this.annualLogListener = annualLogListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AccommodationLog accommodationLog = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.accommodation_log, parent, false);
        }


        LinearLayout accommodationLogCard = convertView.findViewById(R.id.accommodation_log_card);
        ImageView imageView = convertView.findViewById(R.id.accommodationLogImage);
        TextView accommodationName = convertView.findViewById(R.id.accommodationLogTitle);
        TextView accommodationReservationNumber = convertView.findViewById(R.id.accommodationLogReservationNumber);
        TextView accommodationTotalProfit = convertView.findViewById(R.id.accommodationLogTotalProfit);
        Button generateAnnualLogButton = convertView.findViewById(R.id.generateAnnualLogButton);
        if (accommodationLog != null) {
            String photoUrl = accommodationLog.getAccommodationPhoto(); // Replace this with your actual URL string
            // Load the image using Glide
            Glide.with(getContext())
                    .load(photoUrl)
                    .into(imageView);
            accommodationName.setText(accommodationLog.getAccommodationName());
            accommodationReservationNumber.setText(String.valueOf(accommodationLog.getReservationNumber()));
            accommodationTotalProfit.setText(String.valueOf(accommodationLog.getTotalProfit()));
            generateAnnualLogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(annualLogListener!= null){
                        annualLogListener.generateAnnualLog(accommodationLog.getAccommodationId(),accommodationLog.getAccommodationName());
                    }
                }
            });
        }

        return convertView;
    }
    public interface AnnualLogListener {
        void generateAnnualLog(UUID accommodationId, String accommodationName);
    }
}
