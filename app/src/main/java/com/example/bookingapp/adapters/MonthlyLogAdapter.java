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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.example.bookingapp.R;
import com.example.bookingapp.dto.AccommodationLog;
import com.example.bookingapp.dto.AccommodationMonthlyLog;

import java.util.List;

public class MonthlyLogAdapter extends ArrayAdapter<AccommodationMonthlyLog> {
    List<AccommodationMonthlyLog> monthlyLogs;

    public MonthlyLogAdapter(Context context, List<AccommodationMonthlyLog> monthlyLogs){
        super(context, R.layout.host_reservation_card, monthlyLogs);
        this.monthlyLogs = monthlyLogs;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        AccommodationMonthlyLog monthlyLog = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.monthly_log, parent, false);
        }


        ConstraintLayout monthlyLogCard = convertView.findViewById(R.id.monthly_log_card);
        TextView monthName = convertView.findViewById(R.id.monthlyLogMonthName);
        TextView numberOfReservations = convertView.findViewById(R.id.monthlyLogReservationsValue);
        TextView totalProfit = convertView.findViewById(R.id.monthlyLogTotalProfitValue);
        if (monthlyLog != null) {
            monthName.setText(monthlyLog.getMonthName());
            numberOfReservations.setText(String.valueOf(monthlyLog.getNumberOfReservations()));
            totalProfit.setText(String.valueOf(monthlyLog.getTotalProfit()));
        }

        return convertView;
    }
}
