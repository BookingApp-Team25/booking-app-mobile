package com.example.bookingapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bookingapp.R;
import com.example.bookingapp.dto.ReservationRequest;

import java.util.List;
import java.util.Map;

public class GuestReservationsAdapter extends RecyclerView.Adapter<GuestReservationsAdapter.ReservationViewHolder> {

    private List<ReservationRequest> reservationList;
    private Map<String, String> accommodationNames;
    private OnDeleteClickListener onDeleteClickListener;

    public GuestReservationsAdapter(List<ReservationRequest> reservationList, Map<String, String> accommodationNames, OnDeleteClickListener onDeleteClickListener) {
        this.reservationList = reservationList;
        this.accommodationNames = accommodationNames;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public ReservationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reservation, parent, false);
        return new ReservationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservationViewHolder holder, int position) {
        ReservationRequest reservation = reservationList.get(position);

        String accommodationName = accommodationNames.get(reservation.getAccommodationId().toString());
        holder.textViewAccommodationName.setText(accommodationName != null ? accommodationName : "Loading...");

        holder.textViewStartDate.setText("Check-in: " + reservation.getReservedDate().getStartDate().toString());
        holder.textViewEndDate.setText("Check-out: " + reservation.getReservedDate().getEndDate().toString());
        holder.textViewPrice.setText("Price: $" + reservation.getPrice());
        holder.textViewStatus.setText(reservation.getReservationStatus().toString());

        holder.buttonDelete.setOnClickListener(v -> {
            if (onDeleteClickListener != null) {
                onDeleteClickListener.onDeleteClick(reservation.getReservationId().toString());
            }
        });
    }

    @Override
    public int getItemCount() {
        return reservationList.size();
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(String reservationId);
    }

    public void setReservationList(List<ReservationRequest> reservationList) {
        this.reservationList = reservationList;
        notifyDataSetChanged();
    }

    public void setAccommodationNames(Map<String, String> accommodationNames) {
        this.accommodationNames = accommodationNames;
        notifyDataSetChanged();
    }

    static class ReservationViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAccommodationName;
        TextView textViewStartDate;
        TextView textViewEndDate;
        TextView textViewPrice;
        TextView textViewStatus;
        Button buttonDelete;

        public ReservationViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAccommodationName = itemView.findViewById(R.id.textViewAccommodationName);
            textViewStartDate = itemView.findViewById(R.id.textViewStartDate);
            textViewEndDate = itemView.findViewById(R.id.textViewEndDate);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            buttonDelete = itemView.findViewById(R.id.buttonDelete);
        }
    }
}
