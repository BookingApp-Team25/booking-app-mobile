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
import com.example.bookingapp.dto.NotificationResponse;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

public class NotificationListAdapter extends ArrayAdapter<NotificationResponse> {
    List<NotificationResponse> notificationResponses;
    MarkNotificationListener markNotificationListener;

    public NotificationListAdapter(Context context, List<NotificationResponse> notificationResponses, MarkNotificationListener markNotificationListener){
        super(context, R.layout.notification_card, notificationResponses);
        this.notificationResponses = notificationResponses;
        this.markNotificationListener = markNotificationListener;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        NotificationResponse notificationResponse = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.notification_card, parent, false);
        }


        ConstraintLayout notificationLayout = convertView.findViewById(R.id.notificationCard);
        TextView notificationInformation = convertView.findViewById(R.id.notificationInformation);
        TextView notificationTime = convertView.findViewById(R.id.notificationTime);
        FloatingActionButton seenButton = convertView.findViewById(R.id.notificationMarkSeenFloatingButton);
        if (notificationResponse != null) {
            notificationInformation.setText(notificationResponse.getInformation()); // Replace this with your actual URL string
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            notificationTime.setText(notificationResponse.getSendTime().format(formatter));
            seenButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(markNotificationListener != null){
                        markNotificationListener.onMarkNotification(notificationResponse.getId());
                    }
                }
            });
            if(notificationResponse.getSeen()){
                seenButton.hide();
            }
        }

        return convertView;
    }
    public interface MarkNotificationListener {
        void onMarkNotification(UUID notificationId);
    }
}
