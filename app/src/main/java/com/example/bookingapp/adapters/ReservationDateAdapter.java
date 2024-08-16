package com.example.bookingapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.bookingapp.R;
import com.example.bookingapp.dto.ReviewResponse;
import com.example.bookingapp.entities.ReservationDatePeriod;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ReservationDateAdapter extends ArrayAdapter<ReservationDatePeriod> {

    private ArrayList<ReservationDatePeriod> reservedDatePeriods;
    private ReservedDateDeleteListener reservedDateDeleteListener;

    public ReservationDateAdapter(@NonNull Context context, ArrayList<ReservationDatePeriod> reservedDatePeriods, ReservedDateDeleteListener reservedDateDeleteListener) {
        super(context, R.layout.reservation_period,reservedDatePeriods);
        this.reservedDatePeriods = reservedDatePeriods;
        this.reservedDateDeleteListener = reservedDateDeleteListener;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ReservationDatePeriod currentDatePeriod = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.reservation_period, parent, false);
        }
        //ConstraintLayout reservationPeriodCard = convertView.findViewById(R.id.reservation_period_card);
        TextView datePeriodTextView = convertView.findViewById(R.id.reservedDatePeriodTextView);
        RadioButton isSummerRadioButton = convertView.findViewById(R.id.isSummerRadioButton);
        RadioButton isWinterRadioButton = convertView.findViewById(R.id.isWinterRadioButton);
        RadioButton isWeekendRadioButton = convertView.findViewById(R.id.isWeekendRadioButton);
        FloatingActionButton deleteButton = convertView.findViewById(R.id.deleteReservedDatePeriodButton);

        FloatingActionButton deleteReservedDateButton = convertView.findViewById(R.id.deleteReservedDatePeriodButton);
        deleteReservedDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(reservedDateDeleteListener !=null){
                    reservedDateDeleteListener.onDeleteReservedDate(currentDatePeriod);
                }
            }
        });

        if(currentDatePeriod != null){
            datePeriodTextView.setText(currentDatePeriod.getStartDate().toString() + "-" + currentDatePeriod.getEndDate().toString());
            if(currentDatePeriod.isSummerApplied()){
                isSummerRadioButton.toggle();
            }
            if(currentDatePeriod.isWeekendApplied()){
                isWeekendRadioButton.toggle();
            }
            if(currentDatePeriod.isWinterApplied()){
                isWinterRadioButton.toggle();
            }
        }
        return convertView;
    }







    public interface ReservedDateDeleteListener {
        void onDeleteReservedDate(ReservationDatePeriod reservedDate);
    }
}
